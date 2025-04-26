package org.sea.rawg.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DLC(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("background_image") val backgroundImage: String? = null,
    @SerialName("released") val released: String? = null,
    @SerialName("rating") val rating: Float? = null
)