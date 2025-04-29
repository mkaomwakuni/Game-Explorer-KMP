package org.sea.rawg.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import org.sea.rawg.ui.component.ColorSchemeUi

class SettingsViewModel {
    val predefinedColorSchemes = listOf(
        ColorSchemeUi("Purple (Default)", Color(0xFF8750FC)),
        ColorSchemeUi("Red", Color(0xFFD72638)),
        ColorSchemeUi("Teal", Color(0xFF26A69A)),
        ColorSchemeUi("Orange", Color(0xFFFF9800)),
        ColorSchemeUi("Green", Color(0xFF4CAF50))
    )

    private val _isDarkTheme = mutableStateOf(false)
    val isDarkTheme: MutableState<Boolean> = _isDarkTheme

    private val _isSystemTheme = mutableStateOf(true)
    val isSystemTheme: MutableState<Boolean> = _isSystemTheme

    private val _useDynamicColors = mutableStateOf(true)
    val useDynamicColors: MutableState<Boolean> = _useDynamicColors

    private val _selectedColorIndex = mutableStateOf(0)
    val selectedColorIndex: MutableState<Int> = _selectedColorIndex

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
}