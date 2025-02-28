package org.sea.rawg.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.sea.rawg.domain.models.*
import org.sea.rawg.utils.AppConstant

class GamesApiServiceImpl(
    private val client: HttpClient = ApiClient.client
) : GamesApiService {

    /*
     * Generic request builder with common parameters and error handling
     */
    private suspend inline fun <reified T> executeRequest(
        crossinline urlBuilder: URLBuilder.() -> Unit
    ): T {
        try {
            val response = client.get {
                url {
                    takeFrom(AppConstant.BASE_URL)
                    urlBuilder()

                    // Append API key
                    if (!parameters.contains("key")) {
                        parameters.append("key", AppConstant.API_KEY)
                    }
                }

                headers {
                    append(HttpHeaders.Accept, "application/json")
                }
            }

            if (!response.status.isSuccess()) {
                val errorText = response.bodyAsText()
                throw Exception("API error: ${response.status} - $errorText")
            }

            // Try standard deserialization first
            return try {
                response.body()
            } catch (e: Exception) {
                // If that fails, try manual deserialization with our more lenient parser
                val responseText = response.bodyAsText()
                try {
                    // Log error details for debugging
                    println("Original error: ${e.message}")
                    println("Response excerpt: ${responseText.take(200)}...")

                    Json.decodeFromString<T>(responseText)
                } catch (e2: Exception) {
                    throw Exception("Failed to parse API response: ${e2.message}\nOriginal error: ${e.message}")
                }
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }

    // Games endpoints
    override suspend fun getGames(
        page: Int,
        pageSize: Int,
        ordering: String
    ): PagedResponse<Game> {
        return executeRequest {
            appendPathSegments("games")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
            parameters.append("ordering", ordering)
        }
    }

    override suspend fun getGameDetails(gameId: Int): Game {
        return executeRequest {
            appendPathSegments("games", gameId.toString())
        }
    }

    override suspend fun searchGames(
        query: String,
        page: Int
    ): PagedResponse<Game> {
        return executeRequest {
            appendPathSegments("games")
            parameters.append("search", query)
            parameters.append("page", page.toString())
        }
    }

    // Genres endpoints
    override suspend fun getGenres(
        page: Int,
        pageSize: Int
    ): PagedResponse<Genre> {
        return executeRequest {
            appendPathSegments("genres")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
        }
    }

    // Platforms endpoints
    override suspend fun getPlatforms(
        page: Int,
        pageSize: Int
    ): PagedResponse<Platform> {
        return executeRequest {
            appendPathSegments("platforms")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
        }
    }

    // Developers endpoints
    override suspend fun getDevelopers(
        page: Int,
        pageSize: Int
    ): PagedResponse<Developer> {
        return executeRequest {
            appendPathSegments("developers")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
        }
    }

    // Publishers endpoints
    override suspend fun getPublishers(
        page: Int,
        pageSize: Int
    ): PagedResponse<Publisher> {
        return executeRequest {
            appendPathSegments("publishers")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
        }
    }

    // Tags endpoints
    override suspend fun getTags(
        page: Int,
        pageSize: Int
    ): PagedResponse<Tag> {
        return executeRequest {
            appendPathSegments("tags")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
        }
    }

    // Stores endpoints
    override suspend fun getStores(
        page: Int,
        pageSize: Int
    ): PagedResponse<Store> {
        return executeRequest {
            appendPathSegments("stores")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
        }
    }
}