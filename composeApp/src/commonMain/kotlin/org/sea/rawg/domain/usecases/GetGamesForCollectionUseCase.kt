package org.sea.rawg.domain.usecases

import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.PagedResponse
import org.sea.rawg.domain.repository.CollectionRepository

class GetGamesForCollectionUseCase(
    private val repository: CollectionRepository
) {
    suspend operator fun invoke(
        collectionId: Int,
        page: Int = 1,
        pageSize: Int = 20
    ): PagedResponse<Game> {
        return repository.getGamesForCollection(
            collectionId = collectionId,
            page = page,
            pageSize = pageSize
        )
    }
}