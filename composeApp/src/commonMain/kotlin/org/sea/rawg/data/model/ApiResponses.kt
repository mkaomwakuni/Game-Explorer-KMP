package org.sea.rawg.data.model

import kotlinx.serialization.Serializable
import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.Screenshot

@Serializable
data class ScreenshotsResponse(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<Screenshot> = emptyList()
)

@Serializable
data class RedditResponse(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<RedditPost> = emptyList()
)

@Serializable
data class GamesResponse(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<Game> = emptyList()
)