package org.sea.rawg.domain.usecases

import org.sea.rawg.domain.models.Collection
import org.sea.rawg.domain.repository.CollectionRepository

class GetCollectionsUseCase(
    private val repository: CollectionRepository
) {
    suspend operator fun invoke(): List<Collection> {
        return repository.getCollections()
    }
}