package org.sea.rawg.presentation.viewmodels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sea.rawg.data.remote.GamesApiService
import org.sea.rawg.data.remote.GamesApiServiceImpl
import org.sea.rawg.data.repository.RawgRepositoryImpl
import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.usecases.GetGameDetailsUseCase
import org.sea.rawg.presentation.models.DataState
import org.sea.rawg.utils.NetworkResource

/**
 * ViewModel for Game Details screen
 * Using StateFlow for reactive UI updates
 */
class GameDetailsViewModel {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Create repository and use case
    private val apiService: GamesApiService = GamesApiServiceImpl()
    private val repository = RawgRepositoryImpl(apiService)
    private val getGameDetailsUseCase = GetGameDetailsUseCase(repository)

    // UI state
    private val _gameDetails = MutableStateFlow<DataState<Game>>(DataState.Loading)
    val gameDetails: StateFlow<DataState<Game>> = _gameDetails.asStateFlow()

    fun loadGameDetails(gameId: Int) {
        _gameDetails.value = DataState.Loading

        coroutineScope.launch {
            try {
                when (val result = getGameDetailsUseCase(gameId)) {
                    is NetworkResource.Success -> {
                        println("Game details loaded successfully")
                        _gameDetails.value = DataState.Success(result.data)
                    }

                    is NetworkResource.Error -> {
                        println("Error loading game details: ${result.message}")
                        _gameDetails.value = DataState.Error(result.message)
                    }

                    else -> {
                        println("Unexpected state in game details")
                    }
                }
            } catch (e: Exception) {
                println("Exception in loadGameDetails: ${e.message}")
                _gameDetails.value = DataState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}