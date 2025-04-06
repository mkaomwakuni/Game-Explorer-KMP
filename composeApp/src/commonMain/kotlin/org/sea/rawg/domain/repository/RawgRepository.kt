package org.sea.rawg.domain.repository

import org.sea.rawg.domain.models.*
import org.sea.rawg.utils.NetworkResource

/**
 * Repository interface for accessing RAWG API data
 * This is part of the domain layer and should not have implementation details
 */
interface RawgRepository {
    // Games
    suspend fun getGames(
        page: Int,
        pageSize: Int = 20,
        ordering: String = "-released"
    ): NetworkResource<PagedResponse<Game>>

    suspend fun getGameDetails(gameId: Int): NetworkResource<Game>

    suspend fun searchGames(
        query: String,
        page: Int
    ): NetworkResource<PagedResponse<Game>>

    // Genres
    suspend fun getGenres(
        page: Int,
        pageSize: Int = 20
    ): NetworkResource<PagedResponse<Genre>>

    // Platforms
    suspend fun getPlatforms(
        page: Int,
        pageSize: Int = 20
    ): NetworkResource<PagedResponse<Platform>>

    // Developers
    suspend fun getDevelopers(
        page: Int,
        pageSize: Int = 20
    ): NetworkResource<PagedResponse<Developer>>

    // Publishers
    suspend fun getPublishers(
        page: Int,
        pageSize: Int = 20
    ): NetworkResource<PagedResponse<Publisher>>

    // Tags
    suspend fun getTags(
        page: Int,
        pageSize: Int = 20
    ): NetworkResource<PagedResponse<Tag>>

    // Stores
    suspend fun getStores(
        page: Int,
        pageSize: Int = 20
    ): NetworkResource<PagedResponse<Store>>
}