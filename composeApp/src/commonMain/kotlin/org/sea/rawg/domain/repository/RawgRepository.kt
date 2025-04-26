package org.sea.rawg.domain.repository

import org.sea.rawg.domain.models.*
import org.sea.rawg.data.model.DLC
import org.sea.rawg.data.model.RedditPost
import org.sea.rawg.utils.Result

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
    ): Result<PagedResponse<Game>>

    suspend fun getGameDetails(gameId: Int): Result<Game>

    suspend fun searchGames(
        query: String,
        page: Int
    ): Result<PagedResponse<Game>>

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
    ): Result<PagedResponse<Game>>

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
    ): Result<PagedResponse<Game>>

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
    ): Result<PagedResponse<Game>>

    /**
     * Get most anticipated upcoming games (sorted by community interest)
     * @param page Page number for pagination
     * @param pageSize Number of games per page
     * @return List of most anticipated upcoming games
     */
    suspend fun getMostAnticipatedGames(
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Game>>

    // Genres
    suspend fun getGenres(
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Genre>>

    suspend fun getGamesByGenreId(
        genreId: Int,
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Game>>

    // Platforms
    suspend fun getPlatforms(
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Platform>>

    // Developers
    suspend fun getDevelopers(
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Developer>>

    // Publishers
    suspend fun getPublishers(
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Publisher>>

    // Tags
    suspend fun getTags(
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Tag>>

    // Stores
    suspend fun getStores(
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Store>>

    // DLCs
    suspend fun getGameDLCs(
        gameId: Int,
        page: Int = 1,
        pageSize: Int = 5
    ): Result<List<DLC>>

    // Reddit posts
    suspend fun getGameRedditPosts(
        gameName: String,
        count: Int = 5
    ): Result<List<RedditPost>>

    // Screenshots
    suspend fun getGameScreenshots(
        gameId: Int,
        page: Int = 1,
        pageSize: Int = 10
    ): Result<List<Screenshot>>

    // Similar games
    suspend fun getSimilarGames(
        gameId: Int,
        page: Int = 1,
        pageSize: Int = 8
    ): Result<List<Game>>
}