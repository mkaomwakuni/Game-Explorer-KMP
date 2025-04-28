package org.sea.rawg.data.remote

import org.sea.rawg.domain.models.*
import org.sea.rawg.data.model.ScreenshotsResponse
import org.sea.rawg.data.model.RedditResponse

/**
 * Interface for the RAWG API Service (non-premium endpoints)
 */
interface GamesApiService {
    // Games
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

    // Genres
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

    // Platforms
    suspend fun getPlatforms(
        page: Int,
        pageSize: Int
    ): PagedResponse<Platform>

    // Developers
    suspend fun getDevelopers(
        page: Int,
        pageSize: Int
    ): PagedResponse<Developer>

    // Publishers
    suspend fun getPublishers(
        page: Int,
        pageSize: Int
    ): PagedResponse<Publisher>

    // Tags
    suspend fun getTags(
        page: Int,
        pageSize: Int
    ): PagedResponse<Tag>

    // Stores
    suspend fun getStores(page: Int, pageSize: Int): PagedResponse<Store>

    // DLCs - Get a list of DLC's for the game
    suspend fun getGameDLCs(gameId: Int, page: Int, pageSize: Int): PagedResponse<Game>

    // Screenshots - Get screenshots for the game
    suspend fun getGameScreenshots(gameId: Int): ScreenshotsResponse

    // Reddit posts - Get a list of most recent posts from the game's subreddit
    suspend fun getGameRedditPosts(gameId: Int): RedditResponse

    // Similar games - Get a list of visually similar games
    suspend fun getSimilarGames(gameId: Int): PagedResponse<Game>
}