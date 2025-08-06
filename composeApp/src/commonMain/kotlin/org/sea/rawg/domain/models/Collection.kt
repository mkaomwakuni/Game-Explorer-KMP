package org.sea.rawg.domain.models

data class Collection(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val gameType: String,
    val queryFilter: CollectionQueryFilter
)

data class CollectionQueryFilter(
    val ordering: String = "-added",
    val genres: String? = null,
    val tags: String? = null,
    val platforms: String? = null,
    val developers: String? = null,
    val publishers: String? = null,
    val dates: String? = null,
    val ratings: String? = null,
    val metacritic: String? = null
)