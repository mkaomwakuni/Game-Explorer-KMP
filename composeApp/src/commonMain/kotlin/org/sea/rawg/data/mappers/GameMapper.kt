package org.sea.rawg.data.mappers

import org.sea.rawg.data.remote.dto.GameDto
import org.sea.rawg.domain.models.*


object GameMapper {
    fun mapToDomain(dto: GameDto): Game {

        val backgroundImageUrl = when {
            dto.backgroundImage == null -> null
            dto.backgroundImage.startsWith("http://") || dto.backgroundImage.startsWith("https://") -> dto.backgroundImage
            dto.backgroundImage.startsWith("//") -> "https:${dto.backgroundImage}"
            dto.backgroundImage.startsWith("/") -> "https://media.rawg.io${dto.backgroundImage}"
            else -> "https://media.rawg.io/${dto.backgroundImage.removePrefix("media/")}"
        }

        return Game(
            id = dto.id,
            name = dto.name,
            slug = dto.slug,
            released = dto.released,
            description_raw = dto.descriptionRaw,
            background_image = backgroundImageUrl,
            rating = dto.rating.toFloat(),
            ratings_count = dto.ratingsCount,
            playtime = dto.playtime,

            
            platforms = dto.platforms?.map {
                PlatformResponse(
                    platform = it.platform?.let { p ->
                        PlatformDetail(
                            id = p.id,
                            name = p.name,
                            slug = p.slug ?: ""
                        )
                    }
                )
            } ?: emptyList(),

            stores = dto.stores?.map {
                StoreResponse(
                    store = it.store?.let { s ->
                        StoreDetail(
                            id = s.id,
                            name = s.name,
                            slug = s.slug ?: ""
                        )
                    }
                )
            } ?: emptyList(),

            
            developers = dto.developers?.map {
                Developer(
                    id = it.id,
                    name = it.name,
                    slug = it.slug ?: "",
                    games_count = 0,
                    image_background = null
                )
            } ?: emptyList(),

            genres = dto.genres?.map {
                Genre(
                    id = it.id,
                    name = it.name,
                    slug = it.slug ?: "",
                    games_count = 0,
                    image_background = null,
                    description = null
                )
            } ?: emptyList(),

            tags = dto.tags?.map {
                Tag(
                    id = it.id,
                    name = it.name,
                    slug = it.slug ?: "",
                    games_count = 0,
                    image_background = null
                )
            } ?: emptyList(),

            publishers = dto.publishers?.map {
                Publisher(
                    id = it.id,
                    name = it.name,
                    slug = it.slug ?: "",
                    games_count = 0,
                    image_background = null
                )
            } ?: emptyList()
        )
    }
}