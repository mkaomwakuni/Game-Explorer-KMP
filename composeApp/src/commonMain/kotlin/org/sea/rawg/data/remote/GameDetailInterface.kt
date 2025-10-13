package org.sea.rawg.data.remote

import org.sea.rawg.data.model.Game

interface GameDetailInterface {
    suspend fun getGamesDetails(
        gameId: Int
    ): Game
}