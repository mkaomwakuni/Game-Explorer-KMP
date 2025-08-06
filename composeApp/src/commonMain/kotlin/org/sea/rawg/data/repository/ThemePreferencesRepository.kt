package org.sea.rawg.data.repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import org.sea.rawg.data.model.ThemePreferences

/**
 * Repository for managing theme preferences
 *
 * This is a simple in-memory implementation. In a real app,
 * this would be backed by persistent storage like Multiplatform Settings.
 */
class ThemePreferencesRepository {
    
    private val _themePreferences = mutableStateOf(ThemePreferences())
    val themePreferences: MutableState<ThemePreferences> = _themePreferences

    /**
     * Predefined color schemes available for selection
     */
    val predefinedColorSchemes = listOf(
        ColorScheme("Purple (Default)", Color(0xFF8750FC)), 
        ColorScheme("Red", Color(0xFFD72638)), 
        ColorScheme("Teal", Color(0xFF26A69A)), 
        ColorScheme("Orange", Color(0xFFFF9800)), 
        ColorScheme("Green", Color(0xFF4CAF50)), 
    )

    /**
     * Updates the theme mode (Light/Dark/System)
     */
    fun setThemeMode(isDarkTheme: Boolean, isSystemTheme: Boolean) {
        _themePreferences.value = _themePreferences.value.copy(
            isDarkTheme = isDarkTheme,
            isSystemTheme = isSystemTheme
        )
    }

    /**
     * Updates the primary color
     */
    fun setPrimaryColor(color: Color) {
        _themePreferences.value = _themePreferences.value.copy(
            primaryColor = color
        )
    }

    /**
     * Updates the usage of dynamic colors
     */
    fun setUseDynamicColors(useDynamicColors: Boolean) {
        _themePreferences.value = _themePreferences.value.copy(
            useSystemDynamicColors = useDynamicColors
        )
    }

    /**
     * Sets the selected predefined color scheme
     */
    fun setColorScheme(schemeIndex: Int) {
        if (schemeIndex in predefinedColorSchemes.indices) {
            _themePreferences.value = _themePreferences.value.copy(
                selectedColorScheme = schemeIndex,
                primaryColor = predefinedColorSchemes[schemeIndex].color
            )
        }
    }

    /**
     * Resets all theme preferences to defaults
     */
    fun resetToDefaults() {
        _themePreferences.value = ThemePreferences()
    }
}

/**
 * Represents a named color scheme
 */
data class ColorScheme(val name: String, val color: Color)