package org.sea.rawg.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import org.sea.rawg.navigation.BottomNavItem
import org.sea.rawg.navigation.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navigator = rememberNavigator()
    val currentEntry by navigator.currentEntry.collectAsState(null)
    val currentRoute = currentEntry?.route?.route ?: NavigationRoutes.HOME

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                tonalElevation = 0.dp
            ) {
                BottomNavItem.items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navigator.navigate(item.route)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navigator = navigator,
            initialRoute = NavigationRoutes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            scene(NavigationRoutes.HOME) {
                Homepage(navigator = navigator)
            }
            scene(NavigationRoutes.GENRES) {
                GenresScreen(navigator = navigator)
            }
            scene(NavigationRoutes.COLLECTIONS) {
                CollectionsScreen(navigator = navigator)
            }
            scene(NavigationRoutes.PUBLISHERS) {
                PublishersScreen(navigator = navigator)
            }
            scene(NavigationRoutes.SETTINGS) {
                SettingsScreen()
            }
            scene(NavigationRoutes.GAME_DETAILS_WITH_PARAM) { backStackEntry ->
                val gameId = backStackEntry.pathMap["gameId"]?.toIntOrNull()
                if (gameId != null) {
                    GameDetails(navigator = navigator, gameId = gameId)
                }
            }
            scene(NavigationRoutes.UPCOMING_GAMES) {
                UpcomingGamesScreen(navigator = navigator)
            }
            scene(NavigationRoutes.GENRE_DETAILS_WITH_PARAM) { backStackEntry ->
                val genreId = backStackEntry.pathMap["genreId"]?.toIntOrNull()
                if (genreId != null) {
                    GenreDetailsScreen(navigator = navigator, genreId = genreId)
                }
            }
            scene(NavigationRoutes.PUBLISHER_DETAILS_WITH_PARAM) { backStackEntry ->
                val publisherId = backStackEntry.pathMap["publisherId"]?.toIntOrNull()
                if (publisherId != null) {
                    PublisherDetailsScreen(navigator = navigator, publisherId = publisherId)
                }
            }
            scene(NavigationRoutes.COLLECTION_DETAILS_WITH_PARAM) { backStackEntry ->
                val collectionId = backStackEntry.pathMap["collectionId"]?.toIntOrNull()
                if (collectionId != null) {
                    CollectionDetailsScreen(navigator = navigator, collectionId = collectionId)
                }
            }
        }
    }
}