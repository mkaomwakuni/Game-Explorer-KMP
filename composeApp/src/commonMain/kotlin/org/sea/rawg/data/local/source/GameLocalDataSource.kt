package org.sea.rawg.data.local.source

import org.sea.rawg.data.local.entity.GameEntity

/**
 * Local data source interface for game data
 * Abstracts local database operations
 */
interface GameLocalDataSource {
    /**
     * Gets a game by its ID from local storage
     */
    suspend fun getGameById(id: Int): GameEntity?

    /**
     * Saves a game to local storage
     */
    suspend fun saveGame(game: GameEntity)

    /**
     * Gets all locally stored games with optional pagination
     */
    suspend fun getAllGames(page: Int, pageSize: Int): List<GameEntity>

    /**
     * Searches for games by name in local storage
     */
    suspend fun searchGames(query: String): List<GameEntity>

    /**
     * Deletes a game from local storage
     */
    suspend fun deleteGame(id: Int)

    /**
     * Checks if a game exists in local storage
     */
    suspend fun gameExists(id: Int): Boolean
}