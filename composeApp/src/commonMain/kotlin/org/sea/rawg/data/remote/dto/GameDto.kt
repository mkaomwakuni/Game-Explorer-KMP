package org.sea.rawg.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDto(
    val id: Int,
    val slug: String,
    val name: String,
    val released: String? = null,
    val tba: Boolean = false,
    @SerialName("background_image")
    val backgroundImage: String? = null,
    val rating: Double = 0.0,
    @SerialName("rating_top")
    val ratingTop: Int = 0,
    @SerialName("ratings_count")
    val ratingsCount: Int = 0,
    @SerialName("reviews_text_count")
    val reviewsTextCount: Int = 0,
    val added: Int = 0,
    val metacritic: Int? = null,
    val playtime: Int = 0,
    @SerialName("suggestions_count")
    val suggestionsCount: Int = 0,
    val updated: String? = null,
    @SerialName("reviews_count")
    val reviewsCount: Int = 0,
    @SerialName("saturated_color")
    val saturatedColor: String? = null,
    @SerialName("dominant_color")
    val dominantColor: String? = null,
    val platforms: List<PlatformInfoDto>? = emptyList(),
    val stores: List<StoreInfoDto>? = emptyList(),
    val developers: List<DeveloperItemDto>? = emptyList(),
    val genres: List<GenreItemDto>? = emptyList(),
    val tags: List<TagItemDto>? = emptyList(),
    val publishers: List<PublisherItemDto>? = emptyList(),
    @SerialName("esrb_rating")
    val esrbRating: EsrbRatingDto? = null,
    val clip: ClipDto? = null,
    @SerialName("description_raw")
    val descriptionRaw: String? = null,
    val description: String? = null
)

@Serializable
data class PlatformInfoDto(
    val platform: PlatformDto? = null
)

@Serializable
data class PlatformDto(
    val id: Int,
    val name: String,
    val slug: String? = null
)

@Serializable
data class GenreItemDto(
    val id: Int,
    val name: String,
    val slug: String? = null
)

@Serializable
data class DeveloperItemDto(
    val id: Int,
    val name: String,
    val slug: String? = null
)

@Serializable
data class PublisherItemDto(
    val id: Int,
    val name: String,
    val slug: String? = null
)

@Serializable
data class TagItemDto(
    val id: Int,
    val name: String,
    val slug: String? = null
)

@Serializable
data class StoreInfoDto(
    val store: StoreDto? = null
)

@Serializable
data class StoreDto(
    val id: Int,
    val name: String,
    val slug: String? = null
)

@Serializable
data class EsrbRatingDto(
    val id: Int,
    val name: String,
    val slug: String? = null
)

@Serializable
data class ClipDto(
    val clip: String? = null,
    val clips: ClipDataDto? = null
)

@Serializable
data class ClipDataDto(
    val id: Int,
    val name: String,
    val clip: String? = null
)
