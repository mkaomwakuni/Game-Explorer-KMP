package org.sea.rawg.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Requirements(
    val minimum: String? = null,
    val recommended: String? = null
)