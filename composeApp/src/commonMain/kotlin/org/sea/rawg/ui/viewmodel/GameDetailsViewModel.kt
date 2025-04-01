package org.sea.rawg.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
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
import org.sea.rawg.domain.usecases.GetGameDetailsUseCase
import org.sea.rawg.utils.NetworkResource
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import org.sea.rawg.presentation.models.GameState

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
    private val _gameDetails = MutableStateFlow<GameState>(GameState.Loading)
    val gameDetails: StateFlow<GameState>
        get() = _gameDetails.asStateFlow()

    fun loadGameDetails(gameId: Int) {
        _gameDetails.value = GameState.Loading

        coroutineScope.launch {
            try {
                when (val result = getGameDetailsUseCase(gameId)) {
                    is NetworkResource.Success -> {
                        println("Game details loaded successfully")
                        _gameDetails.value = GameState.Success(result.data)
                    }

                    is NetworkResource.Error -> {
                        println("Error loading game details: ${result.message}")
                        _gameDetails.value = GameState.Error(result.message)
                    }

                    else -> {
                        println("Unexpected state in game details")
                    }
                }
            } catch (e: Exception) {
                println("Exception in loadGameDetails: ${e.message}")
                _gameDetails.value = GameState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}