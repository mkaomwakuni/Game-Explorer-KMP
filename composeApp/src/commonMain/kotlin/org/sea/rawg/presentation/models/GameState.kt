package org.sea.rawg.presentation.models

import org.sea.rawg.domain.models.Game

/**
 * UI state representation for game data
 * Used to handle different states in the UI
 */
sealed class GameState {
    object Loading : GameState()
    data class Success(val data: Game) : GameState()
    data class Error(val message: String) : GameState()
}