package org.sea.rawg.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val RAWGShapes = Shapes(
    
    small = RoundedCornerShape(2.dp),

    
    medium = RoundedCornerShape(8.dp),

    
    large = RoundedCornerShape(12.dp),

    
    extraLarge = RoundedCornerShape(16.dp)
)


object CustomShapes {
    val GameCard = RoundedCornerShape(2.dp)
    val BottomSheet = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    val SearchBar = RoundedCornerShape(24.dp)
    val RatingPill = RoundedCornerShape(16.dp)
    val PlatformTag = RoundedCornerShape(2.dp)
}