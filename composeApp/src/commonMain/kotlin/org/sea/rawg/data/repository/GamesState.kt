package org.sea.rawg.data.repository

import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.PagedResponse

sealed class GamesState {
    object Loading : GamesState()
    data class Success(val data: PagedResponse<Game>) : GamesState()
    data class Error(val message: String) : GamesState()
}