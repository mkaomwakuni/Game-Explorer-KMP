package org.sea.rawg.domain.usecases

import org.sea.rawg.domain.models.Collection
import org.sea.rawg.domain.repository.CollectionRepository

class GetCollectionByIdUseCase(
    private val repository: CollectionRepository
) {
    suspend operator fun invoke(collectionId: Int): Collection? {
        return repository.getCollectionById(collectionId)
    }
}