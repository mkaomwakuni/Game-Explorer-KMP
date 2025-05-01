package org.sea.rawg.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import org.sea.rawg.ui.component.ColorSchemeUi

/**
 * Singleton Theme preferences state holder
 */
object ThemeManager {
    // Theme state
    private val _isDarkTheme = mutableStateOf(false)
    val isDarkTheme: MutableState<Boolean> = _isDarkTheme

    private val _isSystemTheme = mutableStateOf(true)
    val isSystemTheme: MutableState<Boolean> = _isSystemTheme

    private val _useDynamicColors = mutableStateOf(true)
    val useDynamicColors: MutableState<Boolean> = _useDynamicColors

    private val _selectedColorIndex = mutableStateOf(0)
    val selectedColorIndex: MutableState<Int> = _selectedColorIndex

    /**
     * Predefined color schemes available for selection
     * Each item is represented by ColorSchemeUi
     */
    val predefinedColorSchemes = listOf(
        ColorSchemeUi("Purple (Default)", Color(0xFF8750FC)), // Default purple
        ColorSchemeUi("Red", Color(0xFFD72638)), // Red
        ColorSchemeUi("Teal", Color(0xFF26A69A)), // Teal
        ColorSchemeUi("Orange", Color(0xFFFF9800)), // Orange
        ColorSchemeUi("Green", Color(0xFF4CAF50))  // Green
    )

    private val _primaryColor = mutableStateOf(predefinedColorSchemes[0].color)
    val primaryColor: MutableState<Color> = _primaryColor

    /**
     * Sets the theme mode
     * @param darkMode True for dark theme, false for light theme
     * @param useSystemSettings True to follow system theme
     */
    fun setThemeMode(darkMode: Boolean, useSystemSettings: Boolean) {
        _isDarkTheme.value = darkMode
        _isSystemTheme.value = useSystemSettings
    }

    /**
     * Updates the primary theme color
     */
    fun setPrimaryColor(color: Color) {
        _primaryColor.value = color
    }

    /**
     * Enables or disables dynamic colors (Material You)
     */
    fun setUseDynamicColors(useDynamicColors: Boolean) {
        _useDynamicColors.value = useDynamicColors
    }

    /**
     * Selects a predefined color scheme by index
     */
    fun selectColorScheme(index: Int) {
        if (index in predefinedColorSchemes.indices) {
            _selectedColorIndex.value = index
            _primaryColor.value = predefinedColorSchemes[index].color
        }
    }

    /**
     * Reset all theme preferences to default values
     */
    fun resetToDefaults() {
        _isDarkTheme.value = false
        _isSystemTheme.value = true
        _useDynamicColors.value = true
        _selectedColorIndex.value = 0
        _primaryColor.value = predefinedColorSchemes[0].color
    }

    /**
     * Creates a color scheme based on current theme preferences
     */
    @Composable
    fun getColorScheme(useDarkTheme: Boolean = effectiveDarkThemeState()): ColorScheme {
        val primary = primaryColor.value

        return if (useDarkTheme) {
            darkColorScheme(
                primary = primary,
                onPrimary = Color.White,
                primaryContainer = primary.copy(alpha = 0.2f),
                onPrimaryContainer = primary,

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
        } else {
            lightColorScheme(
                primary = primary,
                onPrimary = Color.White,
                primaryContainer = primary.copy(alpha = 0.1f),
                onPrimaryContainer = primary,

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
        }
    }

    /**
     * Determines whether dark theme should be used based on settings and system
     */
    @Composable
    fun effectiveDarkThemeState(): Boolean {
        val systemDarkTheme = isSystemInDarkTheme()
        return if (isSystemTheme.value) systemDarkTheme else isDarkTheme.value
    }
}