package org.sea.rawg.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class PagedResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)