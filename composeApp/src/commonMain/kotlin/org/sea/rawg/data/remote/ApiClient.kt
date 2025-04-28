package org.sea.rawg.data.remote

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds
import kotlin.properties.Delegates

object ApiClient {

    val client: HttpClient by lazy {
        createHttpClient()
    }

    var isInitialized: Boolean by Delegates.observable(false) { _, _, _ -> }

    fun createHttpClient(): HttpClient {
        try {
            val httpClient = HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                        useAlternativeNames = false
                    }, contentType = ContentType.Application.Json)
                }

                install(HttpTimeout) {
                    requestTimeoutMillis = 30.seconds.inWholeMilliseconds
                    connectTimeoutMillis = 20.seconds.inWholeMilliseconds
                    socketTimeoutMillis = 30.seconds.inWholeMilliseconds
                }

                install(Logging) {
                    level = LogLevel.NONE
                }

                defaultRequest {
                    contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                    header("User-Agent", "RAWG/API/KMP-GameExplorer-App")
                }
            }
            isInitialized = true
            return httpClient
        } catch (ex: Exception) {
            isInitialized = false
            throw ex
        }
    }

    fun createGamesApiService(client: HttpClient = this.client): GamesApiService {
        return GamesApiServiceImpl(client)
    }
}