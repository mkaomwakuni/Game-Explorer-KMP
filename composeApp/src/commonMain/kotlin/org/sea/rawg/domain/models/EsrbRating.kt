package org.sea.rawg.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class EsrbRating(
    val id: Int = 0,
    val name: String = "",
    val slug: String = ""
)