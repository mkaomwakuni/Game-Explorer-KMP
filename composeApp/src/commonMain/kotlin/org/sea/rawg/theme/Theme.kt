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

val LocalSpacing = compositionLocalOf { Spacing() }

@Composable
fun RAWGTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    
    val themeManager = ThemeManager

    
    val effectiveDarkTheme = if (themeManager.isSystemTheme.value) {
        useDarkTheme
    } else {
        themeManager.isDarkTheme.value
    }

    
    val primaryColor = themeManager.primaryColor.value

    
    val baseColorScheme = if (effectiveDarkTheme) {
        darkColorScheme(
            primary = primaryColor,
            onPrimary = Color.White,
            primaryContainer = primaryColor.copy(alpha = 0.2f),
            onPrimaryContainer = primaryColor,

            secondary = Color(0xFFD72638), 
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFD72638).copy(alpha = 0.2f),
            onSecondaryContainer = Color(0xFFD72638),

            tertiary = Color(0xFF26A69A), 
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
    } else {
        lightColorScheme(
            primary = primaryColor,
            onPrimary = Color.White,
            primaryContainer = primaryColor.copy(alpha = 0.1f),
            onPrimaryContainer = primaryColor,

            secondary = Color(0xFFD72638), 
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFD72638).copy(alpha = 0.1f),
            onSecondaryContainer = Color(0xFFD72638),

            tertiary = Color(0xFF26A69A), 
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
    }

    SystemUiControllerProvider {
        CompositionLocalProvider(
            LocalSpacing provides Spacing()
        ) {
            MaterialTheme(
                colorScheme = baseColorScheme,
                content = content
            )
        }
    }
}


object RAWGTheme {
    val spacing: Spacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current

    val typography: androidx.compose.material3.Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val colorScheme: androidx.compose.material3.ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val shapes: androidx.compose.material3.Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val themeManager: ThemeManager
        get() = ThemeManager
}