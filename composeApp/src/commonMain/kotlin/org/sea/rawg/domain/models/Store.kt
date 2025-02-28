package org.sea.rawg.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Store(
    val id: Int,
    val name: String,
    val slug: String,
    val domain: String?,
    val games_count: Int,
    val image_background: String? = null
)