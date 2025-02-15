package org.sea.rawg.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// Light Theme ColorScheme - Mid/Faint Brown based
private val LightColorScheme = lightColorScheme(
    primary = CoffeeBean,
    onPrimary = White,
    primaryContainer = Tan,
    onPrimaryContainer = DeepBrown,

    secondary = BrownSugar,
    onSecondary = White,
    secondaryContainer = Wheat,
    onSecondaryContainer = DarkBrown,

    tertiary = MutedGold,
    onTertiary = White,
    tertiaryContainer = Linen,
    onTertiaryContainer = DarkBrown,

    background = Sand,
    onBackground = DeepBrown,
    surface = Linen,
    onSurface = DeepBrown,

    surfaceVariant = Wheat,
    onSurfaceVariant = Taupe,
    outline = Khaki,
    outlineVariant = Tan,

    error = BerryRed,
    onError = White,
    errorContainer = BerryRed.copy(alpha = 0.1f),
    onErrorContainer = BerryRed
)

// Dark Theme ColorScheme - Deeper Brown based
private val DarkColorScheme = darkColorScheme(
    primary = LightCoffeeBean,
    onPrimary = DarkSand,
    primaryContainer = DarkTan,
    onPrimaryContainer = LightBeige,

    secondary = LightBrown,
    onSecondary = DarkSand,
    secondaryContainer = DarkBrownSugar,
    onSecondaryContainer = LightBeige,

    tertiary = MutedGold,
    onTertiary = DarkSand,
    tertiaryContainer = DarkKhaki,
    onTertiaryContainer = LightBeige,

    background = DarkSand,
    onBackground = LightBeige,
    surface = DarkLinen,
    onSurface = LightBeige,

    surfaceVariant = DarkWheat,
    onSurfaceVariant = LightBrown,
    outline = DarkTaupe,
    outlineVariant = DarkTan,

    error = BerryRed,
    onError = DarkSand,
    errorContainer = BerryRed.copy(alpha = 0.2f),
    onErrorContainer = LightBeige
)

// Data class to hold dynamic colors that aren't part of the standard Material3 ColorScheme
data class ExtendedColors(
    val ratingHigh: Color,
    val ratingMedium: Color,
    val ratingLow: Color,
    val accent1: Color,
    val accent2: Color,
    val accent3: Color,
)

// Composition Local to provide extended colors
val LocalExtendedColors = compositionLocalOf {
    ExtendedColors(
        ratingHigh = HighRating,
        ratingMedium = MediumRating,
        ratingLow = LowRating,
        accent1 = TerraCotta,
        accent2 = SageGreen,
        accent3 = DustyBlue
    )
}

// Accessor for extended colors
object GameExplorerTheme {
    val extendedColors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}

@Composable
fun GameExplorerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val extendedColors = remember {
        ExtendedColors(
            ratingHigh = HighRating,
            ratingMedium = MediumRating,
            ratingLow = LowRating,
            accent1 = TerraCotta,
            accent2 = SageGreen,
            accent3 = DustyBlue
        )
    }

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = GameExplorerShapes,
            typography = GameExplorerTypography,
            content = content
        )
    }
}