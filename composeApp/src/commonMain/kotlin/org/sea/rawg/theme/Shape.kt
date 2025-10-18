package org.sea.rawg.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Shape system for the RAWG application.
 */
val RAWGShapes = Shapes(
    // Small components like buttons, chips
    small = RoundedCornerShape(2.dp),

    // Medium components like cards, text fields
    medium = RoundedCornerShape(8.dp),

    // Large components like bottom sheets, dialogs
    large = RoundedCornerShape(12.dp),

    // Extra large components like full-screen modals
    extraLarge = RoundedCornerShape(16.dp)
)

/**
 * Custom shape definitions for specific components
 */
object CustomShapes {
    val GameCard = RoundedCornerShape(2.dp)
    val BottomSheet = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    val SearchBar = RoundedCornerShape(24.dp)
    val RatingPill = RoundedCornerShape(16.dp)
    val PlatformTag = RoundedCornerShape(2.dp)
}