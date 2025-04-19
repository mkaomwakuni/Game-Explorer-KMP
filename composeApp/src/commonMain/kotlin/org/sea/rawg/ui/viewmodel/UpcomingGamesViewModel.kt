package org.sea.rawg.ui.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sea.rawg.data.remote.GamesApiService
import org.sea.rawg.data.remote.GamesApiServiceImpl
import org.sea.rawg.data.repository.GamesState
import org.sea.rawg.data.repository.RawgRepositoryImpl
import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.PagedResponse
import org.sea.rawg.utils.NetworkResource

/**
 * ViewModel for the UpcomingGames screen
 * Handles loading, paging, and filtering of upcoming games
 */
class UpcomingGamesViewModel {
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // Create repository
    private val apiService: GamesApiService = GamesApiServiceImpl()
    private val repository = RawgRepositoryImpl(apiService)

    // UI state for upcoming games with pagination
    private val _gamesState = MutableStateFlow<UpcomingGamesState>(UpcomingGamesState.Loading)
    val gamesState: StateFlow<UpcomingGamesState> = _gamesState.asStateFlow()

    // Pagination parameters
    private var currentPage = 1
    private var isLastPage = false
    private val pageSize = 20
    private var loadedGames = mutableListOf<Game>()

    // Filter parameters
    private var filterByFranchise: Boolean = false

    init {
        // Initial load
        loadUpcomingGames()
    }

    /**
     * Load upcoming games using the repository method
     * These are games that will be released in the future
     */
    fun loadUpcomingGames() {
        _gamesState.value = UpcomingGamesState.Loading

        // Reset pagination state
        currentPage = 1
        isLastPage = false
        loadedGames = mutableListOf()

        coroutineScope.launch {
            try {
                // Use the specialized repository method for upcoming games
                val result = repository.getUpcomingGames(
                    page = currentPage,
                    pageSize = pageSize,
                    yearsAhead = 2
                )

                when (result) {
                    is NetworkResource.Success -> {
                        println("Upcoming games loaded successfully")
                        val games = result.data.results
                        loadedGames.addAll(games)
                        isLastPage = result.data.next.isNullOrEmpty()
                        _gamesState.value = UpcomingGamesState.Success(loadedGames.toList())
                    }
                    is NetworkResource.Error -> {
                        println("Error loading upcoming games: ${result.message}")
                        _gamesState.value = UpcomingGamesState.Error(result.message)
                    }
                    else -> {
                        // Handle any other potential states
                        _gamesState.value =
                            UpcomingGamesState.Error("Unknown response from repository")
                    }
                }
            } catch (e: Exception) {
                println("Exception in loadUpcomingGames: ${e.message}")
                _gamesState.value = UpcomingGamesState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    /**
     * Load more upcoming games for pagination
     */
    fun loadMoreUpcomingGames() {
        if (isLastPage || _gamesState.value is UpcomingGamesState.LoadingMore) {
            return
        }

        // Create LoadingMore state with current games
        val currentGames = when (val currentState = _gamesState.value) {
            is UpcomingGamesState.Success -> currentState.games
            else -> return
        }

        _gamesState.value = UpcomingGamesState.LoadingMore(currentGames)

        coroutineScope.launch {
            try {
                // Increment page and load next page
                currentPage++

                val result = repository.getUpcomingGames(
                    page = currentPage,
                    pageSize = pageSize,
                    yearsAhead = 2
                )

                when (result) {
                    is NetworkResource.Success -> {
                        val newGames = result.data.results
                        loadedGames.addAll(newGames)
                        isLastPage = result.data.next.isNullOrEmpty() || newGames.isEmpty()
                        _gamesState.value = UpcomingGamesState.Success(loadedGames.toList())
                    }
                    is NetworkResource.Error -> {
                        // Revert to previous state with current games
                        currentPage-- // Revert page increment
                        _gamesState.value = UpcomingGamesState.Success(loadedGames.toList())
                    }
                    else -> {
                        // Handle any other potential states
                        currentPage-- // Revert page increment
                        _gamesState.value = UpcomingGamesState.Success(loadedGames.toList())
                    }
                }
            } catch (e: Exception) {
                currentPage-- // Revert page increment on error
                _gamesState.value = UpcomingGamesState.Success(loadedGames.toList())
            }
        }
    }

    /**
     * Toggle grouping games by franchise
     */
    fun toggleFranchiseGrouping(enabled: Boolean) {
        filterByFranchise = enabled
        // Refresh the state with new grouping setting without reloading data
        if (_gamesState.value is UpcomingGamesState.Success) {
            _gamesState.value = UpcomingGamesState.Success(loadedGames.toList())
        }
    }

    /**
     * States for the upcoming games screen
     */
    sealed class UpcomingGamesState {
        object Loading : UpcomingGamesState()
        data class LoadingMore(val currentGames: List<Game>) : UpcomingGamesState()
        data class Success(val games: List<Game>) : UpcomingGamesState()
        data class Error(val message: String) : UpcomingGamesState()
    }
}