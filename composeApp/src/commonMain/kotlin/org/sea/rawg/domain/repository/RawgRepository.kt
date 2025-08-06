package org.sea.rawg.domain.repository

import org.sea.rawg.domain.models.*
import org.sea.rawg.data.model.DLC
import org.sea.rawg.data.model.RedditPost
import org.sea.rawg.utils.Result

/**
 * Repository interface for accessing RAWG API data
 */
interface RawgRepository {
    suspend fun getGames(
        page: Int,
        pageSize: Int = 20,
        ordering: String = "-released",
        additionalParams: Map<String, String>
    ): Result<PagedResponse<Game>>

    suspend fun getGameDetails(gameId: Int): Result<Game>

    suspend fun searchGames(
        query: String,
        page: Int
    ): Result<PagedResponse<Game>>

    suspend fun getUpcomingGames(
        page: Int,
        pageSize: Int = 20,
        yearsAhead: Int = 5
    ): Result<PagedResponse<Game>>

    suspend fun getRecentReleases(
        page: Int,
        pageSize: Int = 20,
        daysBack: Int = 30
    ): Result<PagedResponse<Game>>

    suspend fun getGamesByDateRange(
        startDate: String,
        endDate: String,
        page: Int,
        pageSize: Int = 20,
        ordering: String = "-released"
    ): Result<PagedResponse<Game>>

    suspend fun getMostAnticipatedGames(
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Game>>

    suspend fun getGenres(
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Genre>>

    suspend fun getGamesByGenreId(
        genreId: Int,
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Game>>

    suspend fun getPlatforms(
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Platform>>

    suspend fun getDevelopers(
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Developer>>

    suspend fun getPublishers(
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Publisher>>

    suspend fun getTags(
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Tag>>

    suspend fun getStores(
        page: Int,
        pageSize: Int = 20
    ): Result<PagedResponse<Store>>

    suspend fun getGameDLCs(
        gameId: Int,
        page: Int = 1,
        pageSize: Int = 5
    ): Result<List<DLC>>

    suspend fun getGameRedditPosts(
        gameName: String,
        count: Int = 5
    ): Result<List<RedditPost>>

    suspend fun getGameScreenshots(
        gameId: Int,
        page: Int = 1,
        pageSize: Int = 10
    ): Result<List<Screenshot>>

    suspend fun getSimilarGames(
        gameId: Int,
        page: Int = 1,
        pageSize: Int = 8
    ): Result<List<Game>>
}