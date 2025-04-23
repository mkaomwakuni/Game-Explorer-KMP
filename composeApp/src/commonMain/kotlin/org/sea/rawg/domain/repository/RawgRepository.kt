package org.sea.rawg.domain.repository

import org.sea.rawg.domain.models.*
import org.sea.rawg.utils.NetworkResource

/**
 * Repository interface for accessing RAWG API data
 * This is part of the domain layer and should not have implementation details
 */
interface RawgRepository {
    // Games - Enhanced with additional parameters support
    suspend fun getGames(
        page: Int,
        pageSize: Int = 20,
        ordering: String = "-released",
        additionalParams: Map<String, String> = emptyMap()
    ): NetworkResource<PagedResponse<Game>>

    suspend fun getGameDetails(gameId: Int): NetworkResource<Game>

    suspend fun searchGames(
        query: String,
        page: Int
    ): NetworkResource<PagedResponse<Game>>

    // Date-specific game filtering methods

    /**
     * Get upcoming/future games that will be released after the current date
     * @param page Page number for pagination
     * @param pageSize Number of games per page
     * @param yearsAhead How many years into the future to look (default: 5)
     * @return List of upcoming games ordered by release date (earliest first)
     */
    suspend fun getUpcomingGames(
        page: Int,
        pageSize: Int = 20,
        yearsAhead: Int = 5
    ): NetworkResource<PagedResponse<Game>>

    /**
     * Get recent releases from current day backward
     * @param page Page number for pagination
     * @param pageSize Number of games per page
     * @param daysBack How many days back to look for recent releases (default: 30)
     * @return List of recent games ordered by release date (most recent first)
     */
    suspend fun getRecentReleases(
        page: Int,
        pageSize: Int = 20,
        daysBack: Int = 30
    ): NetworkResource<PagedResponse<Game>>

    /**
     * Get games released within a specific date range
     * @param startDate Start date in YYYY-MM-DD format
     * @param endDate End date in YYYY-MM-DD format
     * @param page Page number for pagination
     * @param pageSize Number of games per page
     * @param ordering Sort order (default: "-released" for newest first)
     * @return List of games within the date range
     */
    suspend fun getGamesByDateRange(
        startDate: String,
        endDate: String,
        page: Int,
        pageSize: Int = 20,
        ordering: String = "-released"
    ): NetworkResource<PagedResponse<Game>>

    /**
     * Get most anticipated upcoming games (sorted by community interest)
     * @param page Page number for pagination
     * @param pageSize Number of games per page
     * @return List of most anticipated upcoming games
     */
    suspend fun getMostAnticipatedGames(
        page: Int,
        pageSize: Int = 20
    ): NetworkResource<PagedResponse<Game>>

    // Genres
    suspend fun getGenres(
        page: Int,
        pageSize: Int = 20
    ): NetworkResource<PagedResponse<Genre>>

    suspend fun getGamesByGenreId(
        genreId: Int,
        page: Int,
        pageSize: Int = 20
    ): NetworkResource<PagedResponse<Game>>

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