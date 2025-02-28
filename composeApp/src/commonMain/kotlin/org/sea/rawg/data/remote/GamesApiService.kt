package org.sea.rawg.data.remote

import org.sea.rawg.domain.models.*

/**
 * Interface for the RAWG API Service (non-premium endpoints)
 */
interface GamesApiService {
    // Games
    suspend fun getGames(
        page: Int,
        pageSize: Int = 20,
        ordering: String = "-released"
    ): PagedResponse<Game>

    suspend fun getGameDetails(gameId: Int): Game

    suspend fun searchGames(
        query: String,
        page: Int
    ): PagedResponse<Game>

    // Genres
    suspend fun getGenres(
        page: Int,
        pageSize: Int = 20
    ): PagedResponse<Genre>

    // Platforms
    suspend fun getPlatforms(
        page: Int,
        pageSize: Int = 20
    ): PagedResponse<Platform>

    // Developers
    suspend fun getDevelopers(
        page: Int,
        pageSize: Int = 20
    ): PagedResponse<Developer>

    // Publishers
    suspend fun getPublishers(
        page: Int,
        pageSize: Int = 20
    ): PagedResponse<Publisher>

    // Tags
    suspend fun getTags(
        page: Int,
        pageSize: Int = 20
    ): PagedResponse<Tag>

    // Stores
    suspend fun getStores(
        page: Int,
        pageSize: Int = 20
    ): PagedResponse<Store>
}