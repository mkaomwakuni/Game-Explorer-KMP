package org.sea.rawg.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Platform(
    val id: Int,
    val name: String,
    val slug: String,
    val games_count: Int,
    val image_background: String? = null,
    val image: String? = null,
    val year_start: Int? = null,
    val year_end: Int? = null
)