package org.sea.rawg.data.remote

import org.sea.rawg.domain.models.*
import org.sea.rawg.data.model.ScreenshotsResponse
import org.sea.rawg.data.model.RedditResponse

/**
 * Interface for the RAWG API Service (non-premium endpoints)
 */
interface GamesApiService {
    
    suspend fun getGames(
        page: Int,
        pageSize: Int,
        ordering: String,
        dates: String?,
        platforms: String?,
        developers: String?,
        publishers: String?,
        genres: String?,
        tags: String?,
        search: String?,
        search_exact: String?,
        search_precise: String?
    ): PagedResponse<Game>

    suspend fun getGameDetails(gameId: Int): Game

    suspend fun searchGames(
        query: String,
        page: Int
    ): PagedResponse<Game>

    
    suspend fun getGenres(
        page: Int,
        pageSize: Int
    ): PagedResponse<Genre>

    /**
     * Get games by genre ID
     */
    suspend fun getGamesByGenre(
        genreId: Int,
        page: Int,
        pageSize: Int,
        ordering: String
    ): PagedResponse<Game>

    
    suspend fun getPlatforms(
        page: Int,
        pageSize: Int
    ): PagedResponse<Platform>

    
    suspend fun getDevelopers(
        page: Int,
        pageSize: Int
    ): PagedResponse<Developer>

    
    suspend fun getPublishers(
        page: Int,
        pageSize: Int
    ): PagedResponse<Publisher>

    
    suspend fun getTags(
        page: Int,
        pageSize: Int
    ): PagedResponse<Tag>

    
    suspend fun getStores(page: Int, pageSize: Int): PagedResponse<Store>

    
    suspend fun getGameDLCs(gameId: Int, page: Int, pageSize: Int): PagedResponse<Game>

    
    suspend fun getGameScreenshots(gameId: Int): ScreenshotsResponse

    
    suspend fun getGameRedditPosts(gameId: Int): RedditResponse

    
    suspend fun getSimilarGames(gameId: Int): PagedResponse<Game>
}