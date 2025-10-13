package org.sea.rawg.navigation

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import org.sea.rawg.ui.screens.GameDetails
import org.sea.rawg.ui.screens.Homepage

@Composable
fun NavGraph(navigator: Navigator) {
    NavHost(
        navigator = navigator, initialRoute = "/homepage"
    ) {
        scene(route = "/homepage") {
            Homepage(navigator)
        }
        scene(route = "/details/{id}") { backStackEntry ->
            val id: Int? = backStackEntry.path<Int>("id")
            id?.let {
                GameDetails(navigator, it)
            }
        }
    }
}