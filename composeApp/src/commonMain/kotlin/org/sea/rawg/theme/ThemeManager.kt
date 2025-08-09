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


object ThemeManager {
    
    private val _isDarkTheme = mutableStateOf(false)
    val isDarkTheme: MutableState<Boolean> = _isDarkTheme

    private val _isSystemTheme = mutableStateOf(true)
    val isSystemTheme: MutableState<Boolean> = _isSystemTheme

    private val _useDynamicColors = mutableStateOf(true)
    val useDynamicColors: MutableState<Boolean> = _useDynamicColors

    private val _selectedColorIndex = mutableStateOf(0)
    val selectedColorIndex: MutableState<Int> = _selectedColorIndex

    val predefinedColorSchemes = listOf(
        ColorSchemeUi("Purple (Default)", Color(0xFF8750FC)), 
        ColorSchemeUi("Red", Color(0xFFD72638)), 
        ColorSchemeUi("Teal", Color(0xFF26A69A)), 
        ColorSchemeUi("Orange", Color(0xFFFF9800)), 
        ColorSchemeUi("Green", Color(0xFF4CAF50))  
    )

    private val _primaryColor = mutableStateOf(predefinedColorSchemes[0].color)
    val primaryColor: MutableState<Color> = _primaryColor

    fun setThemeMode(darkMode: Boolean, useSystemSettings: Boolean) {
        _isDarkTheme.value = darkMode
        _isSystemTheme.value = useSystemSettings
    }

    fun setPrimaryColor(color: Color) {
        _primaryColor.value = color
    }

    fun setUseDynamicColors(useDynamicColors: Boolean) {
        _useDynamicColors.value = useDynamicColors
    }

    fun selectColorScheme(index: Int) {
        if (index in predefinedColorSchemes.indices) {
            _selectedColorIndex.value = index
            _primaryColor.value = predefinedColorSchemes[index].color
        }
    }

    fun resetToDefaults() {
        _isDarkTheme.value = false
        _isSystemTheme.value = true
        _useDynamicColors.value = true
        _selectedColorIndex.value = 0
        _primaryColor.value = predefinedColorSchemes[0].color
    }

    @Composable
    fun getColorScheme(useDarkTheme: Boolean = effectiveDarkThemeState()): ColorScheme {
        val primary = primaryColor.value

        return if (useDarkTheme) {
            darkColorScheme(
                primary = primary,
                onPrimary = Color.White,
                primaryContainer = primary.copy(alpha = 0.2f),
                onPrimaryContainer = primary,

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
                primary = primary,
                onPrimary = Color.White,
                primaryContainer = primary.copy(alpha = 0.1f),
                onPrimaryContainer = primary,

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
    }

    @Composable
    fun effectiveDarkThemeState(): Boolean {
        val systemDarkTheme = isSystemInDarkTheme()
        return if (isSystemTheme.value) systemDarkTheme else isDarkTheme.value
    }
}