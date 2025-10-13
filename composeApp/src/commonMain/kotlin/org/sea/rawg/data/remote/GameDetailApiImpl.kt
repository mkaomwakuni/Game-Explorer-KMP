package org.sea.rawg.data.remote

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.sea.rawg.data.model.Game
import org.sea.rawg.utils.AppConstant

class GameDetailApiImpl : GameDetailInterface {
    // The more lenient JSON parser
    private val jsonParser = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
        prettyPrint = false
        encodeDefaults = true
        explicitNulls = false
        allowSpecialFloatingPointValues = true
        useArrayPolymorphism = true
    }

    private fun HttpRequestBuilder.gameDetail(
        gameId: Int,
        api_key: String = AppConstant.API_KEY
    ) {
        url {
            takeFrom(AppConstant.BASE_URL)
            appendPathSegments("games", gameId.toString())
            parameters.append("key", api_key)
        }
        headers {
            append(HttpHeaders.Accept, "application/json")
        }
    }

    override suspend fun getGamesDetails(gameId: Int): Game {
        try {
            val response = client.get {
                gameDetail(gameId)
            }

            if (!response.status.isSuccess()) {
                val errorText = response.bodyAsText()
                throw Exception("API error: ${response.status} - Error fetching game details.")
            }

            // Try standard deserialization first
            try {
                return response.body()
            } catch (e: Exception) {
                // If that fails, try manual deserialization with our more lenient parser
                val responseText = response.bodyAsText()
                try {
                    // Check if "stores" or store-related field is missing
                    if (e.message?.contains("store") == true ||
                        e.message?.contains("id") == true ||
                        responseText.contains("\"stores\":null")
                    ) {
                        println("API response has issues with 'stores' field or related fields.")
                    }

                    // Log more details for debugging
                    println("Original error: ${e.message}")
                    println("Response excerpt: ${responseText.take(200)}...")

                    // Create a special parser just for this response if needed
                    if (e.message?.contains("StoreInfo") == true || e.message?.contains("id") == true) {
                        println("Using ultra-permissive JSON parser for this response")
                        val ultraPermissiveParser = Json {
                            isLenient = true
                            ignoreUnknownKeys = true
                            coerceInputValues = true
                            explicitNulls = false
                            allowSpecialFloatingPointValues = true
                            useArrayPolymorphism = true
                            // New settings to be even more permissive
                            allowStructuredMapKeys = true
                        }
                        return ultraPermissiveParser.decodeFromString(
                            Game.serializer(),
                            responseText
                        )
                    }

                    return jsonParser.decodeFromString(Game.serializer(), responseText)
                } catch (e2: Exception) {
                    throw Exception("Failed to parse API response: ${e2.message}\nOriginal error: ${e.message}")
                }
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }
}