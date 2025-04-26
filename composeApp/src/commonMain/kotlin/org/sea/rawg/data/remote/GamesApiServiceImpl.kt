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
import org.sea.rawg.data.model.ScreenshotsResponse
import org.sea.rawg.data.model.RedditResponse

class GamesApiServiceImpl(
    private val client: HttpClient = ApiClient.client
) : GamesApiService {

    private suspend inline fun <reified T> executeRequest(
        crossinline urlBuilder: URLBuilder.() -> Unit
    ): T {
        try {
            val url = URLBuilder().apply {
                takeFrom(AppConstant.BASE_URL)
                urlBuilder()

                if (!parameters.contains("key")) {
                    parameters.append("key", AppConstant.API_KEY)
                }
            }.build()

            val response = client.get {
                url {
                    takeFrom(url)
                }

                headers {
                    append(HttpHeaders.Accept, "application/json")
                }
            }

            if (!response.status.isSuccess()) {
                val errorText = response.bodyAsText()

                if (response.status == HttpStatusCode.Unauthorized ||
                    response.status == HttpStatusCode.Forbidden ||
                    errorText.contains("key") ||
                    errorText.contains("api") ||
                    errorText.contains("auth") ||
                    errorText.contains("token")
                ) {
                    throw Exception("API key error: Please check your RAWG API key - ${response.status}")
                }

                throw Exception("API error: ${response.status} - $errorText")
            }

            return try {
                val body: T = response.body()
                body
            } catch (e: Exception) {
                val responseText = response.bodyAsText()
                try {
                    val result = Json.decodeFromString<T>(responseText)
                    result
                } catch (e2: Exception) {
                    throw Exception("Failed to parse API response: ${e2.message}\nOriginal error: ${e.message}")
                }
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }

    override suspend fun getGames(
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
    ): PagedResponse<Game> {
        val response: PagedResponseDto<GameDto> = executeRequest {
            appendPathSegments("games")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
            parameters.append("ordering", ordering)

            if (dates != null) parameters.append("dates", dates)
            if (platforms != null) parameters.append("platforms", platforms)
            if (developers != null) parameters.append("developers", developers)
            if (publishers != null) parameters.append("publishers", publishers)
            if (genres != null) parameters.append("genres", genres)
            if (tags != null) parameters.append("tags", tags)
            if (search != null) parameters.append("search", search)
            if (search_exact != null) parameters.append("search_exact", search_exact)
            if (search_precise != null) parameters.append("search_precise", search_precise)
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
                    games_count = 0,
                    image_background = null,
                    image = null,
                    year_start = null,
                    year_end = null
                )
            }
        )
    }

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
                    domain = null,
                    games_count = 0,
                    image_background = null
                )
            }
        )
    }

    override suspend fun getGameDLCs(gameId: Int, page: Int, pageSize: Int): PagedResponse<Game> {
        val response: PagedResponseDto<GameDto> = executeRequest {
            appendPathSegments("games", gameId.toString(), "additions")
            parameters.append("page", page.toString())
            parameters.append("page_size", pageSize.toString())
        }

        return PagedResponse(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = response.results.map { GameMapper.mapToDomain(it) }
        )
    }

    override suspend fun getGameScreenshots(gameId: Int): ScreenshotsResponse {
        return executeRequest {
            appendPathSegments("games", gameId.toString(), "screenshots")
        }
    }

    override suspend fun getGameRedditPosts(gameId: Int): RedditResponse {
        return executeRequest {
            appendPathSegments("games", gameId.toString(), "reddit")
        }
    }

    override suspend fun getSimilarGames(gameId: Int): PagedResponse<Game> {
        val response: PagedResponseDto<GameDto> = executeRequest {
            appendPathSegments("games", gameId.toString(), "game-series")
        }

        return PagedResponse(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = response.results.map { GameMapper.mapToDomain(it) }
        )
    }
}