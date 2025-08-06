package org.sea.rawg.domain.usecases

import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.utils.Result


class GetGameDetailsUseCase(
    private val repository: RawgRepository
) {
    suspend operator fun invoke(gameId: Int): Result<Game> {
        return repository.getGameDetails(gameId)
    }
}