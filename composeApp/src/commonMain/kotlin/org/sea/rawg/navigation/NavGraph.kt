package org.sea.rawg.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import org.sea.rawg.ui.screens.CollectionDetailsScreen
import org.sea.rawg.ui.screens.CollectionsScreen
import org.sea.rawg.ui.screens.GameDetails
import org.sea.rawg.ui.screens.Homepage

@Composable
fun NavGraph(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    initialRoute: String = NavigationRoutes.HOME
) {
    NavHost(
        navigator = navigator,
        initialRoute = initialRoute,
        modifier = modifier
    ) {
        scene(route = NavigationRoutes.HOME) {
            Homepage(navigator)
        }
        scene(route = NavigationRoutes.COLLECTIONS) {
            CollectionsScreen(navigator)
        }
        scene(route = NavigationRoutes.GAME_DETAILS_WITH_PARAM) { backStackEntry ->
            val id: Int? = backStackEntry.path<Int>("gameId")
            id?.let {
                GameDetails(navigator, it)
            }
        }
        scene(route = NavigationRoutes.COLLECTION_DETAILS_WITH_PARAM) { backStackEntry ->
            val id: Int? = backStackEntry.path<Int>("collectionId")
            id?.let {
                CollectionDetailsScreen(navigator, it)
            }
        }
    }
}