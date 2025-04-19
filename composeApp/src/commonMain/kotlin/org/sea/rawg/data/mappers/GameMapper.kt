package org.sea.rawg.data.mappers

import org.sea.rawg.data.remote.dto.GameDto
import org.sea.rawg.domain.models.*

/**
 * Mapper for converting Game DTOs to domain models
 * This provides a clean separation between data and domain layers
 */
object GameMapper {
    /**
     * Maps network DTO to domain model
     */
    fun mapToDomain(dto: GameDto): Game {
        // Debug log for background image
        println("GameMapper: Background image URL: ${dto.backgroundImage}")

        // Ensure URL is properly formatted - sometimes RAWG API returns incomplete URLs
        val backgroundImageUrl = when {
            dto.backgroundImage == null -> null
            dto.backgroundImage.startsWith("http://") || dto.backgroundImage.startsWith("https://") -> dto.backgroundImage
            dto.backgroundImage.startsWith("//") -> "https:${dto.backgroundImage}"
            else -> "https://${dto.backgroundImage}"
        }

        println("GameMapper: Processed background image URL: $backgroundImageUrl")

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

            // Map the nested objects correctly with null safety
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

            // Map to the full model classes with all required fields
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