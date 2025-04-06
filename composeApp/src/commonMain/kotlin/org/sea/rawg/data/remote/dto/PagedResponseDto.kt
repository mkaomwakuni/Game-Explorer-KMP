package org.sea.rawg.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * DTO for paged API responses
 */
@Serializable
data class PagedResponseDto<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)