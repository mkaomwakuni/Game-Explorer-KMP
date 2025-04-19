package org.sea.rawg.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = NavigationRoutes.HOME,
        title = "Home",
        icon = Icons.Default.Gamepad
    )


    object Genres : BottomNavItem(
        route = NavigationRoutes.GENRES,
        title = "Genres",
        icon = Icons.Default.Extension
    )
    object Collections : BottomNavItem(
        route = NavigationRoutes.COLLECTIONS,
        title = "Collections",
        icon = Icons.Default.Category
    )

    object Publishers : BottomNavItem(
        route = NavigationRoutes.PUBLISHERS,
        title = "Publishers",
        icon = Icons.Default.Business
    )

    object Settings : BottomNavItem(
        route = NavigationRoutes.SETTINGS,
        title = "Settings",
        icon = Icons.Default.Settings
    )

    companion object {
        val items = listOf(
            Home,
            Collections,
            Genres,
            Publishers,
            Settings
        )
    }
}