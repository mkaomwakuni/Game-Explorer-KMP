package org.sea.rawg.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val GameExplorerShapes = Shapes(
    // Small components like buttons, chips, etc.
    small = RoundedCornerShape(4.dp),

    // Medium components like cards, dialogs, etc.
    medium = RoundedCornerShape(8.dp),

    // Large components like bottom sheets, modal sheets, etc.
    large = RoundedCornerShape(16.dp),

    // Extra-large components
    extraLarge = RoundedCornerShape(24.dp)
)

// Custom shapes for specific components
val GameCardShape = RoundedCornerShape(12.dp)
val SearchBarShape = RoundedCornerShape(50.dp)
val GameDetailHeaderShape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
val BottomNavShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)