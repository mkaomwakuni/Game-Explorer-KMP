package org.sea.rawg.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
            NavigationBar {
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
                // Extract the gameId parameter from the route parameters
                val gameId = backStackEntry.pathMap["gameId"]?.toIntOrNull()
                // If gameId exists, display the game details screen
                if (gameId != null) {
                    GameDetails(navigator = navigator, gameId = gameId)
                }
            }
            scene(NavigationRoutes.UPCOMING_GAMES) {
                UpcomingGamesScreen(navigator = navigator)
            }
            scene(NavigationRoutes.GENRE_DETAILS_WITH_PARAM) { backStackEntry ->
                // Extract the genreId parameter from the route parameters
                val genreId = backStackEntry.pathMap["genreId"]?.toIntOrNull()
                // If genreId exists, display the genre details screen
                if (genreId != null) {
                    GenreDetailsScreen(navigator = navigator, genreId = genreId)
                }
            }
        }
    }
}