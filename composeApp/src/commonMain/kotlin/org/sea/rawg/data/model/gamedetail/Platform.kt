package org.sea.rawg.data.model.gamedetail

import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    val id: Int = 0,
    val title: String = "",
    val count: Int = 0,
    val percent: Float = 0f
)

@Serializable
data class PlatformInfo(
    val platform: Platform = Platform(),
    val released_at: String? = null,
    val requirements_en: Requirements? = null,
    val requirements_ru: Requirements? = null
)

@Serializable
data class Platform(
    val id: Int = 0,
    val name: String = "",
    val slug: String = ""
)

@Serializable
data class Requirements(
    val minimum: String? = null,
    val recommended: String? = null
)

@Serializable
data class StoreInfo(
    val id: Int = 0,
    val store: Store = Store()
)

@Serializable
data class Store(
    val id: Int = 0,
    val name: String = "",
    val slug: String = ""
)

@Serializable
data class Developer(
    val id: Int = 0,
    val name: String = "",
    val slug: String = ""
)

@Serializable
data class Genre(
    val id: Int = 0,
    val name: String = "",
    val slug: String = ""
)

@Serializable
data class Tag(
    val id: Int = 0,
    val name: String = "",
    val slug: String = ""
)

@Serializable
data class Publisher(
    val id: Int = 0,
    val name: String = "",
    val slug: String = ""
)

@Serializable
data class EsrbRating(
    val id: Int = 0,
    val name: String = "",
    val slug: String = ""
)