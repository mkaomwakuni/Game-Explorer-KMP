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
import org.sea.rawg.utils.NetworkResource
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

class PublishersViewModel(
    private val repository: RawgRepository
) : ViewModel() {

    // State for publishers list
    private val _publishersState = MutableStateFlow<PublishersState>(PublishersState.Loading)
    val publishersState: StateFlow<PublishersState> = _publishersState.asStateFlow()

    // State for search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Loading more indicator for pagination
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    // Current pagination state
    private var currentPage = 1
    private var hasMorePublishers = true
    private var isSearching = false
    private var storedPublishers = listOf<Publisher>()

    // Track if a load operation is already in progress
    private var isLoadingInProgress = false

    // Track API call attempts
    private var loadAttempts = 0
    private val maxAttempts = 3

    init {
        // Don't check isLoadingInProgress during initial load, since we want to force it
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
            // Don't load more if we're at the end or already loading (unless forced)
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

            // Simplified error handling
            if (loadAttempts > maxAttempts) {
                _publishersState.value = PublishersState.Error("Failed to load publishers")
                _isLoadingMore.value = false
                isLoadingInProgress = false
                return
            }

            // Add timeout to avoid indefinite waiting
            val result = withTimeoutOrNull(10000L) { // 10 second timeout
                repository.getPublishers(page = currentPage, pageSize = 20)
            } ?: run {
                NetworkResource.Error("Request timed out. Please check your network connection.")
            }

            when (result) {
                is NetworkResource.Success -> {
                    val newData = result.data

                    // Check if we got empty results but expected data
                    if (newData.results.isEmpty() && newData.count > 0) {
                        // This is unexpected - retry with different page
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

                    // Apply search filter if needed
                    val filteredPublishers = if (searchQuery.value.isNotEmpty()) {
                        combinedPublishers.filter {
                            it.name.contains(searchQuery.value, ignoreCase = true)
                        }
                    } else {
                        combinedPublishers
                    }

                    _publishersState.value = PublishersState.Success(filteredPublishers)
                }

                is NetworkResource.Error -> {
                    _publishersState.value =
                        PublishersState.Error(result.message ?: "Unknown error occurred")
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

        // Filter existing results
        if (_publishersState.value is PublishersState.Success) {
            val filteredPublishers = if (query.isNotEmpty()) {
                storedPublishers.filter { it.name.contains(query, ignoreCase = true) }
            } else {
                storedPublishers
            }
            _publishersState.value = PublishersState.Success(filteredPublishers)
        }
    }

    // Expose whether there are more items to load
    fun hasMoreItems(): Boolean {
        return hasMorePublishers
    }

    // State classes for UI representation
    sealed class PublishersState {
        object Loading : PublishersState()
        data class Success(val publishers: List<Publisher>) : PublishersState()
        data class Error(val message: String) : PublishersState()
    }
}