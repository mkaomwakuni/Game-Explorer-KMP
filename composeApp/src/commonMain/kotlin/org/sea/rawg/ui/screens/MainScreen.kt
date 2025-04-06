package org.sea.rawg.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sea.rawg.navigation.BottomNavItem
import org.sea.rawg.ui.screens.home.Homepage
import org.sea.rawg.ui.screens.collections.CollectionsScreen
import org.sea.rawg.ui.screens.genres.GenesScreen
import org.sea.rawg.ui.screens.publishers.PublishersScreen
import org.sea.rawg.ui.screens.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val selectedItem = remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomNavItem.items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedItem.value == index,
                        onClick = { selectedItem.value = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            // Simple content display based on selected index
            // In a real app, this would use navigation or more complex state management
            val screenText = when (selectedItem.value) {
                0 -> "Home Screen"
                1 -> "Collections Screen"
                2 -> "Genres Screen"
                3 -> "Publishers Screen"
                4 -> "Settings Screen"
                else -> "Unknown Screen"
            }

            Text(text = screenText, style = MaterialTheme.typography.headlineMedium)
        }
    }
}