package org.sea.rawg

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import org.sea.rawg.navigation.NavigationRoutes
import org.sea.rawg.theme.RAWGTheme
import org.sea.rawg.ui.screens.GameDetails
import org.sea.rawg.ui.screens.GenreDetailsScreen
import org.sea.rawg.ui.screens.PublisherDetailsScreen
import org.sea.rawg.ui.screens.WebGamesScreen

@Composable
actual fun App() {
    val navigator = rememberNavigator()

    RAWGTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navigator = navigator,
                initialRoute = NavigationRoutes.HOME
            ) {
                scene(NavigationRoutes.HOME) {
                    WebGamesScreen(navigator = navigator)
                }

                scene(NavigationRoutes.GAME_DETAILS_WITH_PARAM) { backStackEntry ->
                    val gameId = backStackEntry.pathMap["gameId"]?.toIntOrNull() ?: 0
                    GameDetails(navigator = navigator, gameId = gameId)
                }

                scene(NavigationRoutes.GENRE_DETAILS_WITH_PARAM) { backStackEntry ->
                    val genreId = backStackEntry.pathMap["genreId"]?.toIntOrNull() ?: 0
                    GenreDetailsScreen(navigator = navigator, genreId = genreId)
                }

                scene(NavigationRoutes.PUBLISHER_DETAILS_WITH_PARAM) { backStackEntry ->
                    val publisherId = backStackEntry.pathMap["publisherId"]?.toIntOrNull() ?: 0
                    PublisherDetailsScreen(navigator = navigator, publisherId = publisherId)
                }
            }
        }
    }
}