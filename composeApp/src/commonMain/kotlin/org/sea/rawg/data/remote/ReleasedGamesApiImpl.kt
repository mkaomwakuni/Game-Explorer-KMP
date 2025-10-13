package org.sea.rawg.data.remote

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.sea.rawg.data.model.BaseGameModel
import org.sea.rawg.utils.AppConstant

class ReleasedGamesApiImpl : ReleasedGamesInterface {
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

    private fun HttpRequestBuilder.releasedGames(
        page: Int,
        api_key: String = AppConstant.API_KEY
    ) {
        url {
            takeFrom(AppConstant.BASE_URL)
            appendPathSegments("games")
            parameters.append("key", api_key)
            parameters.append("page", page.toString())
            parameters.append("ordering", "-released") // Latest releases first
            parameters.append("page_size", "20")
            // Filter for released games only (no future games)
            parameters.append(
                "dates",
                "2010-01-01,2023-10-13"
            )  // From 2010 to today (hardcoded for now)
            // Include popular platforms
            parameters.append("platforms", "4,187,1,18,186")  // PC, PlayStation, Xbox, etc
        }
        headers {
            append(HttpHeaders.Accept, "application/json")
        }
    }

    override suspend fun releasedGames(page: Int): BaseGameModel {
        try {
            val response = client.get {
                releasedGames(page)
            }

            if (!response.status.isSuccess()) {
                val errorText = response.bodyAsText()
                throw Exception("API error: ${response.status} - Error fetching games.")
            }

            // Try standard deserialization first
            try {
                return response.body()
            } catch (e: Exception) {
                // If that fails, try manual deserialization with our more lenient parser
                val responseText = response.bodyAsText()
                try {
                    // Log more details for debugging
                    println("Original error: ${e.message}")
                    println("Response excerpt: ${responseText.take(200)}...")

                    return jsonParser.decodeFromString(BaseGameModel.serializer(), responseText)
                } catch (e2: Exception) {
                    throw Exception("Failed to parse API response: ${e2.message}\nOriginal error: ${e.message}")
                }
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }
}