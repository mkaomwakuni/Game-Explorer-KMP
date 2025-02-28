package org.sea.rawg.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Developer(
    val id: Int,
    val name: String,
    val slug: String,
    val games_count: Int,
    val image_background: String? = null
)