package org.sea.rawg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sea.rawg.domain.models.PagedResponse
import org.sea.rawg.domain.models.Publisher
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.utils.AppConstant
import org.sea.rawg.utils.Result
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

class PublishersViewModel(
    private val repository: RawgRepository
) : ViewModel() {

    
    private val _publishersState = MutableStateFlow<PublishersState>(PublishersState.Loading)
    val publishersState: StateFlow<PublishersState> = _publishersState.asStateFlow()

    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    
    private var currentPage = 1
    private var hasMorePublishers = true
    private var isSearching = false
    private var storedPublishers = listOf<Publisher>()

    
    private var isLoadingInProgress = false

    
    private var loadAttempts = 0
    private val maxAttempts = 3

    init {
        
        viewModelScope.launch {
            loadPublishersInternal(resetList = true, forceLoad = true)
        }
    }

    fun loadPublishers(resetList: Boolean = false) {
        if (isLoadingInProgress) {
            return
        }

        viewModelScope.launch {
            loadPublishersInternal(resetList, forceLoad = false)
        }
    }

    private suspend fun loadPublishersInternal(
        resetList: Boolean = false,
        forceLoad: Boolean = false
    ) {
        if (isLoadingInProgress && !forceLoad) {
            return
        }

        isLoadingInProgress = true

        if (resetList) {
            currentPage = 1
            hasMorePublishers = true
            loadAttempts = 0
            _publishersState.value = PublishersState.Loading
        } else if (!hasMorePublishers || (_publishersState.value is PublishersState.Loading && !forceLoad)) {
            
            isLoadingInProgress = false
            return
        }

        if (!resetList && _publishersState.value !is PublishersState.Loading) {
            _isLoadingMore.value = true
        }

        val state = _publishersState.value
        val existingPublishers =
            if (state is PublishersState.Success && !resetList) state.publishers else emptyList()

        try {
            loadAttempts++

            
            if (loadAttempts > maxAttempts) {
                _publishersState.value = PublishersState.Error("Failed to load publishers")
                _isLoadingMore.value = false
                isLoadingInProgress = false
                return
            }

            
            val result = withTimeoutOrNull(10000L) { // 10 second timeout
                repository.getPublishers(page = currentPage, pageSize = 20)
            } ?: run {
                Result.Error("Request timed out. Please check your network connection.")
            }

            when (result) {
                is Result.Success<PagedResponse<Publisher>> -> {
                    val newData = result.data

                    
                    if (newData.results.isEmpty() && newData.count > 0) {
                        
                        if (currentPage > 1) {
                            currentPage--
                            loadPublishers(false)
                            return
                        }
                    }

                    val combinedPublishers = existingPublishers + newData.results

                    hasMorePublishers = newData.next != null
                    if (hasMorePublishers) currentPage++

                    storedPublishers = combinedPublishers

                    
                    val filteredPublishers = if (searchQuery.value.isNotEmpty()) {
                        combinedPublishers.filter {
                            it.name.contains(searchQuery.value, ignoreCase = true)
                        }
                    } else {
                        combinedPublishers
                    }

                    _publishersState.value = PublishersState.Success(filteredPublishers)
                }

                is Result.Error -> {
                    _publishersState.value =
                        PublishersState.Error(result.message)
                }

                else -> {
                    _publishersState.value = PublishersState.Error("Unknown error occurred")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _publishersState.value = PublishersState.Error("Exception: ${e.message}")
        } finally {
            _isLoadingMore.value = false
            isLoadingInProgress = false
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query

        
        if (_publishersState.value is PublishersState.Success) {
            val filteredPublishers = if (query.isNotEmpty()) {
                storedPublishers.filter { it.name.contains(query, ignoreCase = true) }
            } else {
                storedPublishers
            }
            _publishersState.value = PublishersState.Success(filteredPublishers)
        }
    }

    
    fun hasMoreItems(): Boolean {
        return hasMorePublishers
    }

    
    sealed class PublishersState {
        object Loading : PublishersState()
        data class Success(val publishers: List<Publisher>) : PublishersState()
        data class Error(val message: String) : PublishersState()
    }
}