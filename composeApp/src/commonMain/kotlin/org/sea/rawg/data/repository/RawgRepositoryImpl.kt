package org.sea.rawg.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sea.rawg.data.remote.GamesApiService
import org.sea.rawg.domain.models.*
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.utils.DateUtils
import org.sea.rawg.utils.NetworkResource

class RawgRepositoryImpl(
    private val apiService: GamesApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : RawgRepository {

    // Games
    override suspend fun getGames(
        page: Int,
        pageSize: Int,
        ordering: String,
        additionalParams: Map<String, String>
    ): NetworkResource<PagedResponse<Game>> {
        return withContext(ioDispatcher) {
            try {
                val dates = additionalParams["dates"]
                val response = apiService.getGames(
                    page = page,
                    pageSize = pageSize,
                    ordering = ordering,
                    dates = dates
                )
                NetworkResource.Success(response)
            } catch (e: Exception) {
                NetworkResource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    override suspend fun getGameDetails(gameId: Int): NetworkResource<Game> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getGameDetails(gameId)
                NetworkResource.Success(response)
            } catch (e: Exception) {
                NetworkResource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    override suspend fun searchGames(
        query: String,
        page: Int
    ): NetworkResource<PagedResponse<Game>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.searchGames(query, page)
                NetworkResource.Success(response)
            } catch (e: Exception) {
                NetworkResource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    override suspend fun getUpcomingGames(
        page: Int,
        pageSize: Int,
        yearsAhead: Int
    ): NetworkResource<PagedResponse<Game>> {
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
    ): NetworkResource<PagedResponse<Game>> {
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
    ): NetworkResource<PagedResponse<Game>> {
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
    ): NetworkResource<PagedResponse<Game>> {
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

    // Genres
    override suspend fun getGenres(
        page: Int,
        pageSize: Int
    ): NetworkResource<PagedResponse<Genre>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getGenres(page, pageSize)
                NetworkResource.Success(response)
            } catch (e: Exception) {
                NetworkResource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    // Platforms
    override suspend fun getPlatforms(
        page: Int,
        pageSize: Int
    ): NetworkResource<PagedResponse<Platform>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getPlatforms(page, pageSize)
                NetworkResource.Success(response)
            } catch (e: Exception) {
                NetworkResource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    // Developers
    override suspend fun getDevelopers(
        page: Int,
        pageSize: Int
    ): NetworkResource<PagedResponse<Developer>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getDevelopers(page, pageSize)
                NetworkResource.Success(response)
            } catch (e: Exception) {
                NetworkResource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    // Publishers
    override suspend fun getPublishers(
        page: Int,
        pageSize: Int
    ): NetworkResource<PagedResponse<Publisher>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getPublishers(page, pageSize)
                NetworkResource.Success(response)
            } catch (e: Exception) {
                NetworkResource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    // Tags
    override suspend fun getTags(page: Int, pageSize: Int): NetworkResource<PagedResponse<Tag>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getTags(page, pageSize)
                NetworkResource.Success(response)
            } catch (e: Exception) {
                NetworkResource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    // Stores
    override suspend fun getStores(
        page: Int,
        pageSize: Int
    ): NetworkResource<PagedResponse<Store>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getStores(page, pageSize)
                NetworkResource.Success(response)
            } catch (e: Exception) {
                NetworkResource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
