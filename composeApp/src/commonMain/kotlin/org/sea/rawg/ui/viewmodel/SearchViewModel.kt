package org.sea.rawg.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.PagedResponse
import org.sea.rawg.domain.repository.RawgRepository

class SearchViewModel(
    private val repository: RawgRepository
) : BaseViewModel() {

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    private val _hasMoreResults = MutableStateFlow(true)
    private val _currentQuery = MutableStateFlow("")
    private val _currentPlatformFilter = MutableStateFlow("")
    private val _currentGenreFilter = MutableStateFlow("")

    val hasFiltersApplied: Boolean
        get() = _currentQuery.value.isNotEmpty() ||
                _currentPlatformFilter.value.isNotEmpty() ||
                _currentGenreFilter.value.isNotEmpty()

    fun searchGames(query: String = "", resetResults: Boolean = true) {
        if (query.isBlank() && _currentQuery.value.isBlank()) {
            return
        }

        if (resetResults) {
            _currentPage.value = 1
            _hasMoreResults.value = true
            _currentQuery.value = query
            _searchState.value = SearchState.Loading
        } else if (!_hasMoreResults.value) {
            return
        } else {
            _searchState.value = SearchState.LoadingMore(_getCurrentResults())
        }

        viewModelScope.launch {
            try {
                val result = repository.searchGames(
                    query = _currentQuery.value,
                    page = _currentPage.value
                )

                result.onSuccess { response ->
                    _hasMoreResults.value = response.next != null

                    if (_currentPage.value > 1 && _searchState.value is SearchState.Success) {
                        val currentGames = (_searchState.value as SearchState.Success).games
                        val newGames = currentGames + response.results
                        _searchState.value = SearchState.Success(newGames)
                    } else {
                        _searchState.value = SearchState.Success(response.results)
                    }

                    if (_hasMoreResults.value) {
                        _currentPage.value++
                    }
                }.onError { message, _ ->
                    _searchState.value = SearchState.Error(message)
                }
            } catch (e: Exception) {
                _searchState.value = SearchState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun setPlatformFilter(platform: String) {
        _currentPlatformFilter.value = platform
    }

    fun setGenreFilter(genre: String) {
        _currentGenreFilter.value = genre
    }

    fun clearFilters() {
        _currentQuery.value = ""
        _currentPlatformFilter.value = ""
        _currentGenreFilter.value = ""
        _currentPage.value = 1
        _hasMoreResults.value = true
        _searchState.value = SearchState.Initial
    }

    fun loadMoreResults() {
        searchGames(_currentQuery.value, false)
    }

    private fun _getCurrentResults(): List<Game> {
        return when (val state = _searchState.value) {
            is SearchState.Success -> state.games
            is SearchState.LoadingMore -> state.currentGames
            else -> emptyList()
        }
    }
}

sealed class SearchState {
    object Initial : SearchState()
    object Loading : SearchState()
    data class LoadingMore(val currentGames: List<Game>) : SearchState()
    data class Success(val games: List<Game>) : SearchState()
    data class Error(val message: String) : SearchState()
}