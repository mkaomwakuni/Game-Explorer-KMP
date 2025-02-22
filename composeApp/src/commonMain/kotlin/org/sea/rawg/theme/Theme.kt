package org.sea.rawg.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// Define LocalSpacing composition local
val LocalSpacing = compositionLocalOf { Spacing() }

/**
 * Light theme color scheme
 */
private val LightColorScheme = lightColorScheme(
    primary = RAWGColors.Primary,
    onPrimary = RAWGColors.Neutral.Light,
    primaryContainer = RAWGColors.Primary.withAlpha(0.1f),
    onPrimaryContainer = RAWGColors.Primary,

    secondary = RAWGColors.Secondary,
    onSecondary = RAWGColors.Neutral.Light,
    secondaryContainer = RAWGColors.Secondary.withAlpha(0.1f),
    onSecondaryContainer = RAWGColors.Secondary,

    tertiary = RAWGColors.Tertiary,
    onTertiary = RAWGColors.Neutral.Light,
    tertiaryContainer = RAWGColors.Tertiary.withAlpha(0.1f),
    onTertiaryContainer = RAWGColors.Tertiary,

    background = RAWGColors.Neutral.Light,
    onBackground = RAWGColors.Neutral.TextPrimary,

    surface = RAWGColors.Neutral.LightSurface,
    onSurface = RAWGColors.Neutral.TextPrimary,
    surfaceVariant = RAWGColors.Neutral.LightSurfaceVariant,
    onSurfaceVariant = RAWGColors.Neutral.TextSecondary,

    error = RAWGColors.Feedback.Error,
    onError = RAWGColors.Neutral.Light,
    errorContainer = RAWGColors.Feedback.Error.withAlpha(0.1f),
    onErrorContainer = RAWGColors.Feedback.Error
)

/**
 * Dark theme color scheme
 */
private val DarkColorScheme = darkColorScheme(
    primary = RAWGColors.Primary,
    onPrimary = RAWGColors.Neutral.Light,
    primaryContainer = RAWGColors.Primary.withAlpha(0.2f),
    onPrimaryContainer = RAWGColors.Primary,

    secondary = RAWGColors.Secondary,
    onSecondary = RAWGColors.Neutral.Light,
    secondaryContainer = RAWGColors.Secondary.withAlpha(0.2f),
    onSecondaryContainer = RAWGColors.Secondary,

    tertiary = RAWGColors.Tertiary,
    onTertiary = RAWGColors.Neutral.Light,
    tertiaryContainer = RAWGColors.Tertiary.withAlpha(0.2f),
    onTertiaryContainer = RAWGColors.Tertiary,

    background = RAWGColors.Neutral.Dark,
    onBackground = RAWGColors.Neutral.TextInversePrimary,

    surface = RAWGColors.Neutral.DarkSurface,
    onSurface = RAWGColors.Neutral.TextInversePrimary,
    surfaceVariant = RAWGColors.Neutral.DarkSurfaceVariant,
    onSurfaceVariant = RAWGColors.Neutral.TextInverseSecondary,

    error = RAWGColors.Feedback.Error,
    onError = RAWGColors.Neutral.Light,
    errorContainer = RAWGColors.Feedback.Error.withAlpha(0.2f),
    onErrorContainer = RAWGColors.Feedback.Error
)

/**
 * RAWG application theme
 * Provides consistent theming across the entire application
 */
@Composable
fun RAWGTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = RAWGTypography.default,
            shapes = RAWGShapes,
            content = content
        )
    }
}

/**
 * Object to access theme values from anywhere in the application
 */
object RAWGTheme {
    /**
     * Access to spacing values from anywhere in the application
     */
    val spacing: Spacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current

    /**
     * Access to typography from the MaterialTheme
     */
    val typography: androidx.compose.material3.Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    /**
     * Access to color scheme from the MaterialTheme
     */
    val colorScheme: androidx.compose.material3.ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    /**
     * Access to shapes from the MaterialTheme
     */
    val shapes: androidx.compose.material3.Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes
}