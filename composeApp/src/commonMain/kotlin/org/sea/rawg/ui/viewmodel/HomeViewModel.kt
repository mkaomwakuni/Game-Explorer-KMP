package org.sea.rawg.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.PagedResponse
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.domain.usecases.GetPopularGamesUseCase
import org.sea.rawg.utils.DateUtils

class HomeViewModel(
    private val repository: RawgRepository,
    private val getPopularGamesUseCase: GetPopularGamesUseCase
) : BaseViewModel() {

    private val _popularGamesState = MutableStateFlow<GamesState>(GamesState.Loading)
    val popularGamesState: StateFlow<GamesState> = _popularGamesState.asStateFlow()
    
    private val _upcomingGamesState = MutableStateFlow<GamesState>(GamesState.Loading)
    val upcomingGamesState: StateFlow<GamesState> = _upcomingGamesState.asStateFlow()
    
    private val _newReleasesState = MutableStateFlow<GamesState>(GamesState.Loading)
    val newReleasesState: StateFlow<GamesState> = _newReleasesState.asStateFlow()
    
    private val _topRatedGamesState = MutableStateFlow<GamesState>(GamesState.Loading)
    val topRatedGamesState: StateFlow<GamesState> = _topRatedGamesState.asStateFlow()

    val popularGamesTitle = "Popular Games"
    val topRatedGamesTitle = "Top Rated Games"
    val recentReleasesTitle = "Recent Releases"
    val upcomingGamesTitle = "Upcoming Games"

    val popularGames = popularGamesState
    val topRatedGames = topRatedGamesState
    val recentReleases = newReleasesState
    val upcomingGames = upcomingGamesState

    fun initialize() {
        viewModelScope.launch {
            launch {
                fetchPopularGames()
            }
            launch {
                fetchUpcomingGames()
            }
            launch {
                fetchRecentReleases()
            }
            launch {
                fetchTopRatedGames()
            }
        }
    }

    fun fetchPopularGames() {
        _popularGamesState.value = GamesState.Loading
        
        launchWithErrorHandling(
            block = {
                getPopularGamesUseCase(page = 1, pageSize = 10)
            },
            onSuccess = { result ->
                result.onSuccess { response ->
                    _popularGamesState.value = filterGamesWithReleaseDates(
                        GamesState.Success(response)
                    )
                }.onError { message, _ ->
                    _popularGamesState.value = GamesState.Error(message)
                }
            },
            onError = { errorState ->
                _popularGamesState.value = GamesState.Error(errorState.message)
            }
        )
    }

    fun fetchUpcomingGames() {
        _upcomingGamesState.value = GamesState.Loading
        
        launchWithErrorHandling(
            block = {
                repository.getUpcomingGames(
                    page = 1, 
                    pageSize = 10
                )
            },
            onSuccess = { result ->
                result.onSuccess { response ->
                    _upcomingGamesState.value = filterGamesWithReleaseDates(
                        GamesState.Success(response)
                    )
                }.onError { message, _ ->
                    _upcomingGamesState.value = GamesState.Error(message)
                }
            },
            onError = { errorState ->
                _upcomingGamesState.value = GamesState.Error(errorState.message)
            }
        )
    }

    fun fetchRecentReleases() {
        _newReleasesState.value = GamesState.Loading
        
        launchWithErrorHandling(
            block = {
                repository.getRecentReleases(
                    page = 1, 
                    pageSize = 10, 
                    daysBack = 90
                )
            },
            onSuccess = { result ->
                result.onSuccess { response ->
                    _newReleasesState.value = filterGamesWithReleaseDates(
                        GamesState.Success(response)
                    )
                }.onError { message, _ ->
                    _newReleasesState.value = GamesState.Error(message)
                }
            },
            onError = { errorState ->
                _newReleasesState.value = GamesState.Error(errorState.message)
            }
        )
    }

    fun fetchTopRatedGames() {
        _topRatedGamesState.value = GamesState.Loading
        
        launchWithErrorHandling(
            block = {
                val endDate = DateUtils.getCurrentDate()
                val startDate = DateUtils.getFutureDate(-5)
                repository.getGames(
                    page = 1, 
                    pageSize = 10, 
                    ordering = "-rating", 
                    additionalParams = mapOf(
                        "dates" to "$startDate,$endDate"
                    )
                )
            },
            onSuccess = { result ->
                result.onSuccess { response ->
                    _topRatedGamesState.value = filterGamesWithReleaseDates(
                        GamesState.Success(response)
                    )
                }.onError { message, _ ->
                    _topRatedGamesState.value = GamesState.Error(message)
                }
            },
            onError = { errorState ->
                _topRatedGamesState.value = GamesState.Error(errorState.message)
            }
        )
    }
    
    private fun filterGamesWithReleaseDates(state: GamesState): GamesState {
        return if (state is GamesState.Success) {
            val filteredGames = state.data.results.filter { 
                !it.released.isNullOrBlank() 
            }
            GamesState.Success(
                PagedResponse(
                    count = filteredGames.size,
                    next = state.data.next,
                    previous = state.data.previous,
                    results = filteredGames
                )
            )
        } else {
            state
        }
    }

    fun resetStates() {
        _popularGamesState.value = GamesState.Loading
        _topRatedGamesState.value = GamesState.Loading
        _newReleasesState.value = GamesState.Loading
        _upcomingGamesState.value = GamesState.Loading
    }

    fun refresh() {
        resetStates()
        initialize()
    }
}

sealed class GamesState {
    object Loading : GamesState()
    data class Success(val data: PagedResponse<Game>) : GamesState()
    data class Error(val message: String) : GamesState()
}
