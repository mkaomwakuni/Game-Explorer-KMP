package org.sea.rawg.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    val id: Int = 0,
    val title: String = "",
    val count: Int = 0,
    val percent: Float = 0f
)