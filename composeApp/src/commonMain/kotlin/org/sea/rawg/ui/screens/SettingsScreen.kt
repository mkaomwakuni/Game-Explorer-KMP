package org.sea.rawg.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.sea.rawg.theme.ThemeManager
import org.sea.rawg.ui.component.ColorSchemeSelector
import org.sea.rawg.ui.component.ColorSchemeUi
import org.sea.rawg.ui.component.SettingItem
import org.sea.rawg.ui.component.SettingItemWithDivider
import org.sea.rawg.ui.component.SwitchSettingItem
import org.sea.rawg.ui.component.ThemeModeSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {

    val themeManager = ThemeManager

    
    val isDarkTheme = themeManager.isDarkTheme.value
    val isSystemTheme = themeManager.isSystemTheme.value
    val useDynamicColors = themeManager.useDynamicColors.value
    val selectedColorIndex = themeManager.selectedColorIndex.value
    var showResetDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {
        
        TopAppBar(
            title = {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.headlineMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "App preferences",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.height(100.dp)
        )

        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 24.dp)
        ) {
            
            ThemeModeSelector(
                isDarkTheme = isDarkTheme,
                isSystemTheme = isSystemTheme,
                onThemeModeChanged = themeManager::setThemeMode
            )

            
            SwitchSettingItem(
                title = "Use Dynamic Colors",
                description = "Use Material You dynamic theming on Android 12+ devices",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Dynamic Colors",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                checked = useDynamicColors,
                onCheckedChange = themeManager::setUseDynamicColors
            )

            
            val predefinedSchemes = listOf(
                ColorSchemeUi("Purple (Default)", Color(0xFF8750FC)),
                ColorSchemeUi("Red", Color(0xFFD72638)),
                ColorSchemeUi("Teal", Color(0xFF26A69A)),
                ColorSchemeUi("Orange", Color(0xFFFF9800)),
                ColorSchemeUi("Green", Color(0xFF4CAF50))
            )

            ColorSchemeSelector(
                colorSchemes = predefinedSchemes,
                selectedIndex = selectedColorIndex,
                onColorSchemeSelected = themeManager::selectColorScheme
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            
            SettingItemWithDivider(
                title = "About RAWG Explorer",
                description = "Version 1.0.0",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "About",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            
            SettingItem(
                title = "Reset Settings",
                description = "Restore default app settings",
                icon = {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "Reset",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onClick = { showResetDialog = true }
            )
        }
    }

    
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Settings") },
            text = { Text("Are you sure you want to reset all settings to their default values?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        themeManager.resetToDefaults()
                        showResetDialog = false
                    }
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}