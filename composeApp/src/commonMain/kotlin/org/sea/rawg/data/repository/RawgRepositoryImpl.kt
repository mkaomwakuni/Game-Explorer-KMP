package org.sea.rawg.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sea.rawg.data.remote.GamesApiService
import org.sea.rawg.domain.models.*
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.utils.DateUtils
import org.sea.rawg.utils.Result
import org.sea.rawg.data.model.DLC
import org.sea.rawg.data.model.RedditPost
import kotlinx.coroutines.delay
import kotlin.random.Random
import org.sea.rawg.data.model.ErrorType
import org.sea.rawg.data.model.RedditComment

class RawgRepositoryImpl(
    private val apiService: GamesApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : RawgRepository {

    override suspend fun getGames(
        page: Int,
        pageSize: Int,
        ordering: String,
        additionalParams: Map<String, String>
    ): Result<PagedResponse<Game>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getGames(
                    page = page,
                    pageSize = pageSize,
                    ordering = ordering,
                    dates = additionalParams["dates"],
                    platforms = additionalParams["platforms"],
                    developers = additionalParams["developers"],
                    publishers = additionalParams["publishers"],
                    genres = additionalParams["genres"],
                    tags = additionalParams["tags"],
                    search = additionalParams["search"],
                    search_exact = additionalParams["search_exact"],
                    search_precise = additionalParams["search_precise"]
                )
                Result.success(response)
            } catch (e: Exception) {
                Result.error(e.message ?: "Unknown error occurred", ErrorType.NETWORK)
            }
        }
    }

    override suspend fun getGameDetails(gameId: Int): Result<Game> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getGameDetails(gameId)
                Result.success(response)
            } catch (e: Exception) {
                Result.error(e.message ?: "Unknown error occurred", ErrorType.NETWORK)
            }
        }
    }

    override suspend fun searchGames(
        query: String,
        page: Int
    ): Result<PagedResponse<Game>> {
        return getGames(
            page = page,
            pageSize = 20,
            ordering = "",
            additionalParams = mapOf(
                "search" to query
            )
        )
    }

    override suspend fun getUpcomingGames(
        page: Int,
        pageSize: Int,
        yearsAhead: Int
    ): Result<PagedResponse<Game>> {
        val tomorrow = DateUtils.getTomorrowDate()
        val futureDate = DateUtils.getFutureDate(yearsAhead)
        
        return getGames(
            page = page,
            pageSize = pageSize,
            ordering = "released",
            additionalParams = mapOf(
                "dates" to "$tomorrow,$futureDate"
            )
        )
    }

    override suspend fun getRecentReleases(
        page: Int,
        pageSize: Int,
        daysBack: Int
    ): Result<PagedResponse<Game>> {
        val today = DateUtils.getCurrentDate()
        val pastDate = DateUtils.getDateDaysAgo(daysBack)
        
        return getGames(
            page = page,
            pageSize = pageSize,
            ordering = "-released",
            additionalParams = mapOf(
                "dates" to "$pastDate,$today"
            )
        )
    }

    override suspend fun getGamesByDateRange(
        startDate: String,
        endDate: String,
        page: Int,
        pageSize: Int,
        ordering: String
    ): Result<PagedResponse<Game>> {
        return getGames(
            page = page,
            pageSize = pageSize,
            ordering = ordering,
            additionalParams = mapOf(
                "dates" to "$startDate,$endDate"
            )
        )
    }

    override suspend fun getMostAnticipatedGames(
        page: Int,
        pageSize: Int
    ): Result<PagedResponse<Game>> {
        val tomorrow = DateUtils.getTomorrowDate()
        val futureDate = DateUtils.getFutureDate(2)
        
        return getGames(
            page = page,
            pageSize = pageSize,
            ordering = "-added",
            additionalParams = mapOf(
                "dates" to "$tomorrow,$futureDate"
            )
        )
    }

    override suspend fun getGenres(
        page: Int,
        pageSize: Int
    ): Result<PagedResponse<Genre>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getGenres(page, pageSize)
                Result.success(response)
            } catch (e: Exception) {
                Result.error(e.message ?: "Unknown error occurred", ErrorType.NETWORK)
            }
        }
    }

    override suspend fun getGamesByGenreId(
        genreId: Int,
        page: Int,
        pageSize: Int
    ): Result<PagedResponse<Game>> {
        return getGames(
            page = page,
            pageSize = pageSize,
            ordering = "-added",
            additionalParams = mapOf(
                "genres" to genreId.toString()
            )
        )
    }

    override suspend fun getPlatforms(
        page: Int,
        pageSize: Int
    ): Result<PagedResponse<Platform>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getPlatforms(page, pageSize)
                Result.success(response)
            } catch (e: Exception) {
                Result.error(e.message ?: "Unknown error occurred", ErrorType.NETWORK)
            }
        }
    }

    override suspend fun getDevelopers(
        page: Int,
        pageSize: Int
    ): Result<PagedResponse<Developer>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getDevelopers(page, pageSize)
                Result.success(response)
            } catch (e: Exception) {
                Result.error(e.message ?: "Unknown error occurred", ErrorType.NETWORK)
            }
        }
    }

    override suspend fun getPublishers(
        page: Int,
        pageSize: Int
    ): Result<PagedResponse<Publisher>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getPublishers(page, pageSize)
                Result.success(response)
            } catch (e: Exception) {
                Result.error(e.message ?: "Unknown error occurred", ErrorType.NETWORK)
            }
        }
    }

    override suspend fun getTags(page: Int, pageSize: Int): Result<PagedResponse<Tag>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getTags(page, pageSize)
                Result.success(response)
            } catch (e: Exception) {
                Result.error(e.message ?: "Unknown error occurred", ErrorType.NETWORK)
            }
        }
    }

    override suspend fun getStores(
        page: Int,
        pageSize: Int
    ): Result<PagedResponse<Store>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getStores(page, pageSize)
                Result.success(response)
            } catch (e: Exception) {
                Result.error(e.message ?: "Unknown error occurred", ErrorType.NETWORK)
            }
        }
    }

    override suspend fun getGameDLCs(
        gameId: Int,
        page: Int,
        pageSize: Int
    ): Result<List<DLC>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getGameDLCs(gameId, page, pageSize)

                val dlcs = response.results.map { game ->
                    DLC(
                        id = game.id.toString(),
                        name = game.name,
                        backgroundImage = game.background_image,
                        released = game.released,
                        rating = game.rating
                    )
                }

                Result.success(dlcs)
            } catch (e: Exception) {
                Result.error(e.message ?: "Unknown error occurred", ErrorType.NETWORK)
            }
        }
    }

    override suspend fun getGameRedditPosts(
        gameName: String,
        count: Int
    ): Result<List<RedditPost>> {
        return withContext(ioDispatcher) {
            try {
                val searchResponse = apiService.searchGames(
                    query = gameName,
                    page = 1
                )

                if (searchResponse.results.isEmpty()) {
                    return@withContext Result.success(emptyList<RedditPost>())
                }

                val gameId = searchResponse.results.first().id

                val response = apiService.getGameRedditPosts(gameId)

                val posts = response.results.map { post ->
                    RedditPost(
                        id = post.id ?: "",
                        name = post.name ?: "",
                        url = post.url ?: "",
                        subreddit = post.subreddit,
                        createdAt = post.createdAt
                    )
                }.take(count)

                Result.success(posts)
            } catch (e: Exception) {
                Result.error(e.message ?: "Unknown error occurred", ErrorType.NETWORK)
            }
        }
    }

    override suspend fun getGameScreenshots(
        gameId: Int,
        page: Int,
        pageSize: Int
    ): Result<List<Screenshot>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getGameScreenshots(gameId)
                Result.success(response.results)
            } catch (e: Exception) {
                Result.error(e.message ?: "Unknown error occurred", ErrorType.NETWORK)
            }
        }
    }

    override suspend fun getSimilarGames(
        gameId: Int,
        page: Int,
        pageSize: Int
    ): Result<List<Game>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getSimilarGames(gameId)
                Result.success(response.results)
            } catch (e: Exception) {
                Result.error(e.message ?: "Unknown error occurred", ErrorType.NETWORK)
            }
        }
    }

    private fun padWithZero(number: Int): String {
        return if (number < 10) "0$number" else number.toString()
    }
}
