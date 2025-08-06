package org.sea.rawg.data.local.entity

/**
 * Database entity for Game
 * This represents how the game is stored locally
 *
 * Note: In a real implementation, this would be annotated with
 * SQLDelight or other KMP database solution's annotations
 */
data class GameEntity(
    val id: Int,
    val name: String,
    val slug: String?,
    val released: String?,
    val descriptionRaw: String?,
    val backgroundImage: String?,
    val rating: Double,
    val ratingsCount: Int,
    val playtime: Int,
    
    
    
)