package org.sea.rawg.domain.models

import kotlinx.serialization.Serializable

/**
 * Domain model for Game
 * Simplified version for initial implementation
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
    val platforms: List<String> = emptyList(),
    val stores: List<String> = emptyList(),
    val developers: List<String> = emptyList(),
    val genres: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val publishers: List<String> = emptyList(),
    val description_raw: String? = null
)