package org.sea.rawg.data.remote

import org.sea.rawg.data.model.BaseGameModel

interface ReleasedGamesInterface {
    suspend fun releasedGames(page: Int): BaseGameModel
}