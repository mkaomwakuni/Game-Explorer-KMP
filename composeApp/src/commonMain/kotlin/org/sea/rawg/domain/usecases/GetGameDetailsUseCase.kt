package org.sea.rawg.domain.usecases

import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.utils.NetworkResource

/**
 * Use case for retrieving details of a specific game
 * Following clean architecture principles
 */
class GetGameDetailsUseCase(
    private val repository: RawgRepository
) {
    suspend operator fun invoke(gameId: Int): NetworkResource<Game> {
        return repository.getGameDetails(gameId)
    }
}