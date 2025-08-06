package org.sea.rawg.data.local.source

import org.sea.rawg.data.local.entity.GameEntity


interface GameLocalDataSource {
    suspend fun getGameById(id: Int): GameEntity?

    suspend fun saveGame(game: GameEntity)

    suspend fun getAllGames(page: Int, pageSize: Int): List<GameEntity>

    suspend fun searchGames(query: String): List<GameEntity>

    suspend fun deleteGame(id: Int)

    suspend fun gameExists(id: Int): Boolean
}