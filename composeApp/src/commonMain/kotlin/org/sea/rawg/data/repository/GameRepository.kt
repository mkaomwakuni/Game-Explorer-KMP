package org.sea.rawg.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.sea.rawg.data.model.BaseGameModel
import org.sea.rawg.data.model.Game
import org.sea.rawg.data.remote.GameDetailApiImpl
import org.sea.rawg.data.remote.ReleasedGamesApiImpl

class GameRepository {
    private val releasedGamesApi = ReleasedGamesApiImpl()
    private val gameDetailApi = GameDetailApiImpl()

    // Simple in-memory cache
    private var cachedGamesResponse: BaseGameModel? = null
    private val gameDetailsCache = mutableMapOf<Int, Game>()

    fun getReleasedGames(page: Int): Flow<GamesState> = flow {
        emit(GamesState.Loading)

        // Return cached data first if available (for first page only)
        if (page == 1 && cachedGamesResponse != null) {
            emit(GamesState.Success(cachedGamesResponse!!, isFromCache = true))
        }

        try {
            val result = releasedGamesApi.releasedGames(page)
            // Cache first page results
            if (page == 1) {
                cachedGamesResponse = result
            }
            emit(GamesState.Success(result))
        } catch (e: Exception) {
            // If there's no cache and API call failed, emit error
            if (page != 1 || cachedGamesResponse == null) {
                emit(
                    GamesState.Error(
                        e.message ?: "Unable to load games. Please check your connection."
                    )
                )
            }
            // Otherwise, we already emitted the cached data so no need to emit error
        }
    }

    fun getGameDetails(gameId: Int): Flow<GameState> = flow {
        emit(GameState.Loading)

        // Return cached data first if available
        val cachedGame = gameDetailsCache[gameId]
        if (cachedGame != null) {
            emit(GameState.Success(cachedGame, isFromCache = true))
        }

        try {
            val result = gameDetailApi.getGamesDetails(gameId)
            // Update cache
            gameDetailsCache[gameId] = result
            emit(GameState.Success(result))
        } catch (e: Exception) {
            // If there's no cache and API call failed, emit error
            if (cachedGame == null) {
                emit(
                    GameState.Error(
                        e.message ?: "Unable to load game details. Please check your connection."
                    )
                )
            }
            // Otherwise, we already emitted the cached data so no need to emit error
        }
    }
}

// State classes for UI states
sealed class GamesState {
    object Loading : GamesState()
    data class Success(val data: BaseGameModel, val isFromCache: Boolean = false) : GamesState()
    data class Error(val message: String) : GamesState()
}

sealed class GameState {
    object Loading : GameState()
    data class Success(val data: Game, val isFromCache: Boolean = false) : GameState()
    data class Error(val message: String) : GameState()
}