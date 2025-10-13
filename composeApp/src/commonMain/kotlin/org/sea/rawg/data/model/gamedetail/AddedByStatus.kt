package org.sea.rawg.data.model.gamedetail

import kotlinx.serialization.Serializable

@Serializable
data class AddedByStatus(
    val yet: Int? = null,
    val owned: Int? = null,
    val beaten: Int? = null,
    val toplay: Int? = null,
    val dropped: Int? = null,
    val playing: Int? = null
)