package org.sea.rawg.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for Genre from network API
 */
@Serializable
data class GenreDto(
    val id: Int,
    val name: String,
    val slug: String? = null,
    val games_count: Int = 0,
    val image_background: String? = null
)