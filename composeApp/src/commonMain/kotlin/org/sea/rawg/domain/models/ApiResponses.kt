import kotlinx.serialization.Serializable
import org.sea.rawg.domain.models.Screenshot

@kotlinx.serialization.Serializable
data class Genre(
    val id: Int = 0,
    val name: String = "",
    val slug: String = "",
    val games_count: Int = 0,
    val image_background: String? = null,
    val description: String? = null
)

@kotlinx.serialization.Serializable
data class Developer(
    val id: Int = 0,
    val name: String = "",
    val slug: String = "",
    val games_count: Int = 0,
    val image_background: String? = null
)

@kotlinx.serialization.Serializable
data class Publisher(
    val id: Int = 0,
    val name: String = "",
    val slug: String = "",
    val games_count: Int = 0,
    val image_background: String? = null
)

@kotlinx.serialization.Serializable
data class Tag(
    val id: Int = 0,
    val name: String = "",
    val slug: String = "",
    val games_count: Int = 0,
    val image_background: String? = null
)

@kotlinx.serialization.Serializable
data class Store(
    val id: Int = 0,
    val name: String = "",
    val slug: String = "",
    val games_count: Int = 0,
    val image_background: String? = null,
    val domain: String? = null
)

@kotlinx.serialization.Serializable
data class ScreenshotsResponse(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<Screenshot> = emptyList()
)

@kotlinx.serialization.Serializable
data class RedditPost(
    val id: String? = null,
    val name: String? = null,
    val text: String? = null,
    val image: String? = null,
    val url: String? = null,
    val username: String? = null,
    val subreddit: String? = null,
    val created: String? = null
)

@Serializable
data class RedditResponse(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<RedditPost> = emptyList()
)