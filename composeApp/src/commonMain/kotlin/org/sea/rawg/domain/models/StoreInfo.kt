package org.sea.rawg.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class StoreInfo(
    val id: Int = 0,
    val store: Store
)