package org.sea.rawg.navigation

object NavigationRoutes {
    const val HOME = "home"
    const val COLLECTIONS = "collections"
    const val GENRES = "genres"
    const val PUBLISHERS = "publishers"
    const val SETTINGS = "settings"
    const val GAME_DETAILS = "game_details"
    const val SEARCH = "search"
    const val UPCOMING_GAMES = "upcoming_games"

    // Routes with parameters
    const val GAME_DETAILS_WITH_PARAM = "game_details/{gameId}"
    const val GENRE_DETAILS_WITH_PARAM = "genre_details/{genreId}"
    const val PUBLISHER_DETAILS_WITH_PARAM = "publisher_details/{publisherId}"

    // Functions to create routes with parameters
    fun gameDetailsRoute(gameId: Int): String = "game_details/$gameId"
    fun genreDetailsRoute(genreId: Int): String = "genre_details/$genreId"
    fun publisherDetailsRoute(publisherId: Int): String = "publisher_details/$publisherId"
}