package org.sea.rawg.theme

import androidx.compose.ui.graphics.Color


object RAWGColors {
    
    val Primary = Color(0xFFF24E1E)
    val Secondary = Color(0xFF2B2B2B)
    val Tertiary = Color(0xFF6F49AC)

    
    object Neutral {
        
        val Light = Color(0xFFFFFFFF)
        val LightVariant = Color(0xFFF5F5F5)
        val LightSurface = Color(0xFFF8F8F8)
        val LightSurfaceVariant = Color(0xFFEEEEEE)

        
        val Dark = Color(0xFF121212)
        val DarkVariant = Color(0xFF1E1E1E)
        val DarkSurface = Color(0xFF242424)
        val DarkSurfaceVariant = Color(0xFF2C2C2C)

        
        val TextPrimary = Color(0xFF000000)
        val TextSecondary = Color(0xFF636363)
        val TextTertiary = Color(0xFF9E9E9E)
        val TextDisabled = Color(0xFFBDBDBD)
        val TextInversePrimary = Color(0xFFFFFFFF)
        val TextInverseSecondary = Color(0xFFD4D4D4)
    }

    
    object Feedback {
        val Success = Color(0xFF4CAF50)
        val Error = Color(0xFFE53935)
        val Warning = Color(0xFFFFC107)
        val Info = Color(0xFF2196F3)
    }

    
    object Rating {
        val Exceptional = Color(0xFF66CC33)
        val Recommended = Color(0xFF66CCFF)
        val Meh = Color(0xFFFFCC33)
        val Skip = Color(0xFFFF0000)
    }

    
    object Platform {
        val PlayStation = Color(0xFF003791)
        val Xbox = Color(0xFF107C10)
        val Nintendo = Color(0xFFE60012)
        val PC = Color(0xFF212121)
        val Mobile = Color(0xFF3DDC84)
    }
}

val Sand = Color(0xFFF5EDE0)
val Linen = Color(0xFFF0E6D9)
val Wheat = Color(0xFFE9D9C2)
val Tan = Color(0xFFD9C2A5)
val Khaki = Color(0xFFBFA989)
val BrownSugar = Color(0xFFAA8976)
val Taupe = Color(0xFF8C7262)
val CoffeeBean = Color(0xFF6F5743)
val DarkBrown = Color(0xFF4A3B2F)
val DeepBrown = Color(0xFF362A20)

val DarkSand = Color(0xFF352E25)
val DarkLinen = Color(0xFF403830)
val DarkWheat = Color(0xFF4F4438)
val DarkTan = Color(0xFF5E5045)
val DarkKhaki = Color(0xFF695A4E)
val DarkBrownSugar = Color(0xFF7A6B5D)
val DarkTaupe = Color(0xFF8A7C6D)
val LightCoffeeBean = Color(0xFF9E9081)
val LightBrown = Color(0xFFB2A596)
val LightBeige = Color(0xFFD0C3B2)

val TerraCotta = Color(0xFFD17A5D)
val SageGreen = Color(0xFF8C9F80)
val DustyBlue = Color(0xFF7C93A1)
val MutedGold = Color(0xFFCCA352)
val BerryRed = Color(0xFFAE4C5E)

val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Transparent = Color(0x00000000)

val HighRating = Color(0xFF7BAF6E)
val MediumRating = Color(0xFFF5D247)
val LowRating = Color(0xFFE05E48)

fun Color.withAlpha(alpha: Float): Color {
    return this.copy(alpha = alpha)
}