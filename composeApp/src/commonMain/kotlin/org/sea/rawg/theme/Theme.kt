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
    primary = Color(0xFF8750FC), // Primary purple
    onPrimary = Color.White,
    primaryContainer = Color(0xFF8750FC).copy(alpha = 0.1f),
    onPrimaryContainer = Color(0xFF8750FC),

    secondary = Color(0xFFD72638), // Secondary red
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD72638).copy(alpha = 0.1f),
    onSecondaryContainer = Color(0xFFD72638),

    tertiary = Color(0xFF26A69A), // Tertiary teal
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF26A69A).copy(alpha = 0.1f),
    onTertiaryContainer = Color(0xFF26A69A),

    background = Color.White,
    onBackground = Color(0xFF1A1A1A),

    surface = Color(0xFFF5F5F5),
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFEEEEEE),
    onSurfaceVariant = Color(0xFF555555),

    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFB00020).copy(alpha = 0.1f),
    onErrorContainer = Color(0xFFB00020)
)

/**
 * Dark theme color scheme
 */
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8750FC), // Primary purple
    onPrimary = Color.White,
    primaryContainer = Color(0xFF8750FC).copy(alpha = 0.2f),
    onPrimaryContainer = Color(0xFF8750FC),

    secondary = Color(0xFFD72638), // Secondary red
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD72638).copy(alpha = 0.2f),
    onSecondaryContainer = Color(0xFFD72638),

    tertiary = Color(0xFF26A69A), // Tertiary teal
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF26A69A).copy(alpha = 0.2f),
    onTertiaryContainer = Color(0xFF26A69A),

    background = Color(0xFF121212),
    onBackground = Color(0xFFEEEEEE),

    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFEEEEEE),
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = Color(0xFFAAAAAA),

    error = Color(0xFFCF6679),
    onError = Color.White,
    errorContainer = Color(0xFFCF6679).copy(alpha = 0.2f),
    onErrorContainer = Color(0xFFCF6679)
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

    SystemUiControllerProvider {
        // No need for LocalSystemUiController here since platform-specific implementations
        // will handle the status bar appropriately
        CompositionLocalProvider(LocalSpacing provides Spacing()) {
            MaterialTheme(
                colorScheme = colorScheme,
                content = content
            )
        }
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