package org.sea.rawg.domain.models

import kotlinx.serialization.Serializable

/**
 * Domain model for Game
 * Includes support to ignore unknown keys and parse nested structures
 */
@Serializable
data class Game(
    val id: Int = 0,
    val slug: String = "",
    val name: String = "",
    val released: String? = null,
    val background_image: String? = null,
    val rating: Float = 0f,
    val ratings_count: Int = 0,
    val playtime: Int = 0,

    // Make all list fields nullable with default empty lists
    val platforms: List<PlatformResponse>? = emptyList(),
    val stores: List<StoreResponse>? = emptyList(),
    val developers: List<Developer>? = emptyList(),
    val genres: List<Genre>? = emptyList(),
    val tags: List<Tag>? = emptyList(),
    val publishers: List<Publisher>? = emptyList(),
    val description_raw: String? = null,
    val screenshots: List<Screenshot>? = emptyList(),
    val similar_games: List<Game>? = emptyList(),
    val website: String? = null
) {
    // Convenience methods to safely access list fields
    fun getPlatformNames(): List<String> =
        platforms?.mapNotNull { it.platform?.name } ?: emptyList()

    fun getStoreNames(): List<String> = stores?.mapNotNull { it.store?.name } ?: emptyList()
    fun getDeveloperNames(): List<String> = developers?.map { it.name } ?: emptyList()
    fun getGenreNames(): List<String> = genres?.map { it.name } ?: emptyList()
    fun getTagNames(): List<String> = tags?.map { it.name } ?: emptyList()
    fun getPublisherNames(): List<String> = publishers?.map { it.name } ?: emptyList()
}


@Serializable
data class PlatformResponse(
    val platform: PlatformDetail? = null
)

@Serializable
data class PlatformDetail(
    val id: Int = 0,
    val name: String = "",
    val slug: String = ""
)

@Serializable
data class StoreResponse(
    val store: StoreDetail? = null
)

@Serializable
data class StoreDetail(
    val id: Int = 0,
    val name: String = "",
    val slug: String = ""
)

@Serializable
data class Screenshot(
    val id: Int = 0,
    val image: String = "",
    val hidden: Boolean = false,
    val width: Int = 0,
    val height: Int = 0
)
