package org.sea.rawg.data.model

import androidx.compose.ui.graphics.Color

/**
 * Data class representing the user's theme preferences
 */
data class ThemePreferences(
    val isDarkTheme: Boolean = false,
    val isSystemTheme: Boolean = true,
    val primaryColor: Color = Color(0xFF8750FC),
    val useSystemDynamicColors: Boolean = true,
    val selectedColorScheme: Int = 0
    )