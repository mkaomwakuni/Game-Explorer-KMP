package org.sea.rawg.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Represents a named color scheme for UI
 */
data class ColorSchemeUi(val name: String, val color: Color)

@Composable
fun SettingItem(
    title: String,
    description: String? = null,
    icon: @Composable (() -> Unit)? = null,
    endContent: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = if (endContent != null) 16.dp else 0.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }
            }

            if (endContent != null) {
                endContent()
            }
        }
    }
}

/**
 * Setting item with a switch.
 */
@Composable
fun SwitchSettingItem(
    title: String,
    description: String? = null,
    icon: @Composable (() -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    SettingItem(
        title = title,
        description = description,
        icon = icon,
        endContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        },
        onClick = { onCheckedChange(!checked) }
    )
}

/**
 * Setting item with a divider below it
 */
@Composable
fun SettingItemWithDivider(
    title: String,
    description: String? = null,
    icon: @Composable (() -> Unit)? = null,
    endContent: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    Column {
        SettingItem(
            title = title,
            description = description,
            icon = icon,
            endContent = endContent,
            onClick = onClick
        )
        HorizontalDivider(
            modifier = Modifier.padding(start = if (icon != null) 72.dp else 16.dp, end = 16.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
    }
}

/**
 * Color picker item for a single color.
 */
@Composable
fun ColorPickerItem(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderAlpha by animateFloatAsState(if (selected) 1f else 0f)

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = borderAlpha),
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = if (color.luminance() > 0.5f) Color.Black else Color.White
            )
        }
    }
}

/**
 * Row of color scheme options
 */
@Composable
fun ColorSchemeSelector(
    colorSchemes: List<ColorSchemeUi>,
    selectedIndex: Int,
    onColorSchemeSelected: (Int) -> Unit
) {
    Column {
        Text(
            text = "Theme Color",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            colorSchemes.forEachIndexed { index, scheme ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(56.dp)
                ) {
                    ColorPickerItem(
                        color = scheme.color,
                        selected = index == selectedIndex,
                        onClick = { onColorSchemeSelected(index) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = scheme.name,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (index == selectedIndex) 1f else 0.6f)
                    )
                }
            }
        }

        
        if (selectedIndex in colorSchemes.indices) {
            val selectedColor = colorSchemes[selectedIndex].color
            ColorPreview(selectedColor)
        }
    }
}

/**
 * Shows a preview of how UI elements look with the selected color
 */
@Composable
fun ColorPreview(color: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Preview",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = color)
                ) {
                    Text("Button")
                }

                
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    color = color.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Primary Container",
                            style = MaterialTheme.typography.bodyMedium,
                            color = color
                        )
                    }
                }
            }
        }
    }
}

/**
 * Theme mode selector (Light/Dark/System)
 */
@Composable
fun ThemeModeSelector(
    isDarkTheme: Boolean,
    isSystemTheme: Boolean,
    onThemeModeChanged: (isDark: Boolean, isSystem: Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )

            RadioButtonSettingItem(
                title = "System Default",
                selected = isSystemTheme,
                onClick = { onThemeModeChanged(isDarkTheme, true) }
            )

            RadioButtonSettingItem(
                title = "Light Theme",
                selected = !isSystemTheme && !isDarkTheme,
                onClick = { onThemeModeChanged(false, false) }
            )

            RadioButtonSettingItem(
                title = "Dark Theme",
                selected = !isSystemTheme && isDarkTheme,
                onClick = { onThemeModeChanged(true, false) }
            )
        }
    }
}

/**
 * Setting item with a radio button.
 */
@Composable
fun RadioButtonSettingItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}