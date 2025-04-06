package org.sea.rawg.data.repository

import org.sea.rawg.domain.models.Game

/**
 * State class for Game data in UI
 */
sealed class GameState {
    object Loading : GameState()
    data class Success(val data: Game) : GameState()
    data class Error(val message: String) : GameState()
}