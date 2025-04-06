package org.sea.rawg.data.mappers

import org.sea.rawg.data.remote.dto.GameDto
import org.sea.rawg.domain.models.Game

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
            // Extract data from nested objects
            platforms = dto.platforms.mapNotNull { it.platform?.name },
            genres = dto.genres.map { it.name },
            developers = dto.developers.map { it.name },
            publishers = dto.publishers.map { it.name },
            tags = dto.tags.map { it.name },
            stores = dto.stores.mapNotNull { it.store?.name }
        )
    }
}