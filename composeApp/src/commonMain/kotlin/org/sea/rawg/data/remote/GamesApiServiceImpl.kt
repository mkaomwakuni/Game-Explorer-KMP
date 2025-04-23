package org.sea.rawg.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.sea.rawg.data.mappers.GameMapper
import org.sea.rawg.domain.models.*
import org.sea.rawg.data.remote.dto.*
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
        ordering: String,
        dates: String?
    ): PagedResponse<Game> {
        val response: PagedResponseDto<GameDto> = executeRequest {
            appendPathSegments("games")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
            parameters.append("ordering", ordering)
            if (dates != null) {
                parameters.append("dates", dates)
            }
        }

        // Debug logging for image URLs
        response.results.forEach { gameDto ->
            println("Game image URL: ${gameDto.backgroundImage}")
        }

        return PagedResponse(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = response.results.map { GameMapper.mapToDomain(it) }
        )
    }

    override suspend fun getGameDetails(gameId: Int): Game {
        val response: GameDto = executeRequest {
            appendPathSegments("games", gameId.toString())
        }
        return GameMapper.mapToDomain(response)
    }

    override suspend fun searchGames(
        query: String,
        page: Int
    ): PagedResponse<Game> {
        val response: PagedResponseDto<GameDto> = executeRequest {
            appendPathSegments("games")
            parameters.append("search", query)
            parameters.append("page", page.toString())
        }
        return PagedResponse(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = response.results.map { GameMapper.mapToDomain(it) }
        )
    }

    // Genres endpoints
    override suspend fun getGenres(
        page: Int,
        pageSize: Int
    ): PagedResponse<Genre> {
        val response: PagedResponseDto<GenreDto> = executeRequest {
            appendPathSegments("genres")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
        }
        return PagedResponse(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = response.results.map {
                Genre(
                    id = it.id,
                    name = it.name,
                    slug = it.slug ?: "",
                    games_count = it.games_count ?: 0,
                    image_background = it.image_background
                )
            }
        )
    }

    override suspend fun getGamesByGenre(
        genreId: Int,
        page: Int,
        pageSize: Int,
        ordering: String
    ): PagedResponse<Game> {
        val response: PagedResponseDto<GameDto> = executeRequest {
            appendPathSegments("games")
            parameters.append("genres", genreId.toString())
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
            parameters.append("ordering", ordering)
        }

        return PagedResponse(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = response.results.map { GameMapper.mapToDomain(it) }
        )
    }

    // Platforms endpoints
    override suspend fun getPlatforms(
        page: Int,
        pageSize: Int
    ): PagedResponse<Platform> {
        val response: PagedResponseDto<PlatformDto> = executeRequest {
            appendPathSegments("platforms")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
        }
        return PagedResponse(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = response.results.map {
                Platform(
                    id = it.id,
                    name = it.name,
                    slug = it.slug ?: "",
                    games_count = 0, // Default value
                    image_background = null,
                    image = null,
                    year_start = null,
                    year_end = null
                )
            }
        )
    }

    // Developers endpoints
    override suspend fun getDevelopers(
        page: Int,
        pageSize: Int
    ): PagedResponse<Developer> {
        val response: PagedResponseDto<DeveloperDto> = executeRequest {
            appendPathSegments("developers")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
        }
        return PagedResponse(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = response.results.map {
                Developer(
                    id = it.id,
                    name = it.name,
                    slug = it.slug ?: "",
                    games_count = it.games_count ?: 0,
                    image_background = it.image_background
                )
            }
        )
    }

    // Publishers endpoints
    override suspend fun getPublishers(
        page: Int,
        pageSize: Int
    ): PagedResponse<Publisher> {
        val response: PagedResponseDto<PublisherDto> = executeRequest {
            appendPathSegments("publishers")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
        }
        return PagedResponse(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = response.results.map {
                Publisher(
                    id = it.id,
                    name = it.name,
                    slug = it.slug ?: "",
                    games_count = it.games_count ?: 0,
                    image_background = it.image_background
                )
            }
        )
    }

    // Tags endpoints
    override suspend fun getTags(
        page: Int,
        pageSize: Int
    ): PagedResponse<Tag> {
        val response: PagedResponseDto<TagDto> = executeRequest {
            appendPathSegments("tags")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
        }
        return PagedResponse(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = response.results.map {
                Tag(
                    id = it.id,
                    name = it.name,
                    slug = it.slug ?: "",
                    games_count = it.games_count ?: 0,
                    image_background = it.image_background
                )
            }
        )
    }

    // Stores endpoints
    override suspend fun getStores(
        page: Int,
        pageSize: Int
    ): PagedResponse<Store> {
        val response: PagedResponseDto<StoreDto> = executeRequest {
            appendPathSegments("stores")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
        }
        return PagedResponse(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = response.results.map {
                Store(
                    id = it.id,
                    name = it.name,
                    slug = it.slug ?: "",
                    domain = null, // Default value
                    games_count = 0, // Default value
                    image_background = null
                )
            }
        )
    }
}