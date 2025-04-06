package org.sea.rawg.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for Publisher from network API
 */
@Serializable
data class PublisherDto(
    val id: Int,
    val name: String,
    val slug: String? = null,
    val games_count: Int = 0,
    val image_background: String? = null
)