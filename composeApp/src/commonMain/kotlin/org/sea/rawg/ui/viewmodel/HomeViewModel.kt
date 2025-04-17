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
import org.sea.rawg.domain.models.PagedResponse
import org.sea.rawg.utils.DateUtils
import org.sea.rawg.utils.NetworkResource

class HomeViewModel {
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val apiService: GamesApiService = GamesApiServiceImpl()
    private val repository = RawgRepositoryImpl(apiService)

    private val _upcomingGames = MutableStateFlow<GamesState>(GamesState.Loading)
    val upcomingGames: StateFlow<GamesState> = _upcomingGames.asStateFlow()

    private val _topRatedGames = MutableStateFlow<GamesState>(GamesState.Loading)
    val topRatedGames: StateFlow<GamesState> = _topRatedGames.asStateFlow()

    private val _recentReleases = MutableStateFlow<GamesState>(GamesState.Loading)
    val recentReleases: StateFlow<GamesState> = _recentReleases.asStateFlow()

    private val _popularGames = MutableStateFlow<GamesState>(GamesState.Loading)
    val popularGames: StateFlow<GamesState> = _popularGames.asStateFlow()

    val upcomingGamesTitle = "Upcoming Games (Future Releases)"
    val recentReleasesTitle = "Recent Releases (Past 90 Days)"
    val topRatedGamesTitle = "Top Rated Games"
    val popularGamesTitle = "Popular Games"

    init {
        refresh()
    }

    private fun filterGamesWithReleaseDates(state: GamesState): GamesState {
        if (state is GamesState.Success) {
            val filteredResults = state.data.results.filter { game ->
                game.released != null
            }

            val filteredResponse = PagedResponse(
                count = filteredResults.size,
                next = state.data.next,
                previous = state.data.previous,
                results = filteredResults
            )

            return GamesState.Success(filteredResponse)
        }
        return state
    }

    private fun loadUpcomingGames() {
        _upcomingGames.value = GamesState.Loading

        coroutineScope.launch {
            try {
                val result = repository.getUpcomingGames(page = 1, pageSize = 10)

                when (result) {
                    is NetworkResource.Success -> {
                        println("Upcoming games loaded successfully")
                        _upcomingGames.value = filterGamesWithReleaseDates(GamesState.Success(result.data))
                    }
                    is NetworkResource.Error -> {
                        println("Error loading upcoming games: ${result.message}")
                        _upcomingGames.value = GamesState.Error(result.message)
                    }
                    else -> {
                        println("Unexpected state in upcoming games")
                    }
                }
            } catch (e: Exception) {
                println("Exception in loadUpcomingGames: ${e.message}")
                _upcomingGames.value = GamesState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun loadTopRatedGames() {
        _topRatedGames.value = GamesState.Loading

        coroutineScope.launch {
            try {
                // Get date range for top rated games - last 5 years up to today
                val endDate = DateUtils.getCurrentDate()
                val startDate = DateUtils.getFutureDate(-5) // 5 years in the past

                // Games sorted by rating from highest to lowest with date filtering
                val result = repository.getGames(
                    page = 1,
                    pageSize = 10,
                    ordering = "-rating",
                    additionalParams = mapOf(
                        "dates" to "$startDate,$endDate"
                    )
                )

                when (result) {
                    is NetworkResource.Success -> {
                        println("Top rated games loaded successfully")
                        _topRatedGames.value = filterGamesWithReleaseDates(GamesState.Success(result.data))
                    }
                    is NetworkResource.Error -> {
                        println("Error loading top rated games: ${result.message}")
                        _topRatedGames.value = GamesState.Error(result.message)
                    }
                    else -> {
                        println("Unexpected state in top rated games")
                    }
                }
            } catch (e: Exception) {
                println("Exception in loadTopRatedGames: ${e.message}")
                _topRatedGames.value = GamesState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun loadRecentReleases() {
        _recentReleases.value = GamesState.Loading

        coroutineScope.launch {
            try {
                val result = repository.getRecentReleases(page = 1, pageSize = 10, daysBack = 90)

                when (result) {
                    is NetworkResource.Success -> {
                        println("Recent releases loaded successfully")
                        _recentReleases.value = filterGamesWithReleaseDates(GamesState.Success(result.data))
                    }
                    is NetworkResource.Error -> {
                        println("Error loading recent releases: ${result.message}")
                        _recentReleases.value = GamesState.Error(result.message)
                    }
                    else -> {
                        println("Unexpected state in recent releases")
                    }
                }
            } catch (e: Exception) {
                println("Exception in loadRecentReleases: ${e.message}")
                _recentReleases.value = GamesState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun loadPopularGames() {
        _popularGames.value = GamesState.Loading

        coroutineScope.launch {
            try {
                val result = repository.getMostAnticipatedGames(page = 1, pageSize = 10)

                when (result) {
                    is NetworkResource.Success -> {
                        println("Popular games loaded successfully")
                        _popularGames.value = filterGamesWithReleaseDates(GamesState.Success(result.data))
                    }
                    is NetworkResource.Error -> {
                        println("Error loading popular games: ${result.message}")
                        _popularGames.value = GamesState.Error(result.message)
                    }
                    else -> {
                        println("Unexpected state in popular games")
                    }
                }
            } catch (e: Exception) {
                println("Exception in loadPopularGames: ${e.message}")
                _popularGames.value = GamesState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun refresh() {
        loadUpcomingGames()
        loadTopRatedGames()
        loadRecentReleases()
        loadPopularGames()
    }
}
