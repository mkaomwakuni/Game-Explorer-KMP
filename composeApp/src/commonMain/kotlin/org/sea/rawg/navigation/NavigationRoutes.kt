package org.sea.rawg.navigation

object NavigationRoutes {
    const val HOME = "home"
    const val COLLECTIONS = "collections"
    const val GENRES = "genres"
    const val PUBLISHERS = "publishers"
    const val SETTINGS = "settings"
    const val GAME_DETAILS = "game_details"
    const val SEARCH = "search"

    // Routes with parameters
    const val GAME_DETAILS_WITH_PARAM = "game_details/{gameId}"

    // Functions to create routes with parameters
    fun gameDetailsRoute(gameId: Int): String = "game_details/$gameId"
}