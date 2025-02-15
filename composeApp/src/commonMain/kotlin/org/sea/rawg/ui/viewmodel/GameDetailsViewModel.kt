package org.sea.rawg.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.sea.rawg.data.repository.GameRepository
import org.sea.rawg.data.repository.GameState

class GameDetailsViewModel(
    private val repository: GameRepository = GameRepository()
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    // State for game details
    val gameDetails = mutableStateOf<GameState>(GameState.Loading)

    fun loadGameDetails(gameId: Int) {
        viewModelScope.launch {
            repository.getGameDetails(gameId).collectLatest { state ->
                gameDetails.value = state
            }
        }
    }
}