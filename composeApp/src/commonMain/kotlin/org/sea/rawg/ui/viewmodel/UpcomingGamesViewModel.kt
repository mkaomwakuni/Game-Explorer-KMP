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
import org.sea.rawg.utils.Result
import org.koin.core.component.inject
import org.sea.rawg.domain.repository.RawgRepository

class UpcomingGamesViewModel(
    private val repository: RawgRepository
) : BaseViewModel() {
    private val _gamesState = MutableStateFlow<UpcomingGamesState>(UpcomingGamesState.Loading)
    val gamesState: StateFlow<UpcomingGamesState> = _gamesState.asStateFlow()

    private var currentPage = 1
    private var isLastPage = false
    private val pageSize = 20
    private var loadedGames = mutableListOf<Game>()

    private var filterByFranchise: Boolean = false

    init {
        loadUpcomingGames()
    }

    fun loadUpcomingGames() {
        _gamesState.value = UpcomingGamesState.Loading

        currentPage = 1
        isLastPage = false
        loadedGames = mutableListOf()

        viewModelScope.launch {
            try {
                val result = repository.getUpcomingGames(
                    page = currentPage,
                    pageSize = pageSize,
                    yearsAhead = 2
                )

                when (result) {
                    is Result.Success<PagedResponse<Game>> -> {
                        val games = result.data.results
                        loadedGames.addAll(games)
                        isLastPage = result.data.next.isNullOrEmpty()
                        _gamesState.value = UpcomingGamesState.Success(loadedGames.toList())
                    }
                    is Result.Error -> {
                        _gamesState.value = UpcomingGamesState.Error(result.message)
                    }
                    else -> {
                        _gamesState.value =
                            UpcomingGamesState.Error("Unknown response from repository")
                    }
                }
            } catch (e: Exception) {
                _gamesState.value = UpcomingGamesState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun loadMoreUpcomingGames() {
        if (isLastPage || _gamesState.value is UpcomingGamesState.LoadingMore) {
            return
        }

        val currentGames = when (val currentState = _gamesState.value) {
            is UpcomingGamesState.Success -> currentState.games
            else -> return
        }

        _gamesState.value = UpcomingGamesState.LoadingMore(currentGames)

        viewModelScope.launch {
            try {
                currentPage++

                val result = repository.getUpcomingGames(
                    page = currentPage,
                    pageSize = pageSize,
                    yearsAhead = 2
                )

                when (result) {
                    is Result.Success<PagedResponse<Game>> -> {
                        val newGames = result.data.results
                        loadedGames.addAll(newGames)
                        isLastPage = result.data.next.isNullOrEmpty() || newGames.isEmpty()
                        _gamesState.value = UpcomingGamesState.Success(loadedGames.toList())
                    }
                    is Result.Error -> {
                        currentPage--
                        _gamesState.value = UpcomingGamesState.Success(loadedGames.toList())
                    }
                    else -> {
                        currentPage--
                        _gamesState.value = UpcomingGamesState.Success(loadedGames.toList())
                    }
                }
            } catch (e: Exception) {
                currentPage--
                _gamesState.value = UpcomingGamesState.Success(loadedGames.toList())
            }
        }
    }

    fun toggleFranchiseGrouping(enabled: Boolean) {
        filterByFranchise = enabled
        if (_gamesState.value is UpcomingGamesState.Success) {
            _gamesState.value = UpcomingGamesState.Success(loadedGames.toList())
        }
    }

    sealed class UpcomingGamesState {
        object Loading : UpcomingGamesState()
        data class LoadingMore(val currentGames: List<Game>) : UpcomingGamesState()
        data class Success(val games: List<Game>) : UpcomingGamesState()
        data class Error(val message: String) : UpcomingGamesState()
    }
}