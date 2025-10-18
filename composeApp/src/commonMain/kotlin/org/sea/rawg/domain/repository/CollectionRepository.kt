package org.sea.rawg.domain.repository

import org.sea.rawg.domain.models.Collection
import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.PagedResponse

/**
 * Repository for accessing game collections and games within collections
 */
interface CollectionRepository {
    /**
     * Get a list of all available collections
     */
    suspend fun getCollections(): List<Collection>

    /**
     * Get a specific collection by its ID
     */
    suspend fun getCollectionById(collectionId: Int): Collection?

    /**
     * Get games for a specific collection with pagination
     */
    suspend fun getGamesForCollection(
        collectionId: Int,
        page: Int,
        pageSize: Int
    ): PagedResponse<Game>
}