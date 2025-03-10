package org.sea.rawg.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sea.rawg.data.remote.GamesApiService
import org.sea.rawg.data.remote.GamesApiServiceImpl
import org.sea.rawg.domain.models.*
import org.sea.rawg.utils.NetworkResource

/**
 * Repository for accessing RAWG API data
 * Provides a clean interface for the domain layer
 */
class RawgRepository(
    private val apiService: GamesApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    // Games
    suspend fun getGames(
        page: Int,
        pageSize: Int = 20,
        ordering: String = "-released"
    ): NetworkResource<PagedResponse<Game>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getGames(page, pageSize, ordering)
                NetworkResource.Success(response)
            } catch (e: Exception) {
                NetworkResource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    suspend fun getGameDetails(gameId: Int): NetworkResource<Game> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getGameDetails(gameId)
                NetworkResource.Success(response)
            } catch (e: Exception) {
                NetworkResource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    suspend fun searchGames(query: String, page: Int): NetworkResource<PagedResponse<Game>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.searchGames(query, page)
                NetworkResource.Success(response)
            } catch (e: Exception) {
                NetworkResource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    // Genres
    suspend fun getGenres(page: Int, pageSize: Int = 20): NetworkResource<PagedResponse<Genre>> {
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
    suspend fun getPlatforms(
        page: Int,
        pageSize: Int = 20
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
    suspend fun getDevelopers(
        page: Int,
        pageSize: Int = 20
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
    suspend fun getPublishers(
        page: Int,
        pageSize: Int = 20
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
    suspend fun getTags(page: Int, pageSize: Int = 20): NetworkResource<PagedResponse<Tag>> {
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
    suspend fun getStores(page: Int, pageSize: Int = 20): NetworkResource<PagedResponse<Store>> {
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