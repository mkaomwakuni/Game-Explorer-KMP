package org.sea.rawg.domain.models

import kotlinx.serialization.Serializable
import org.sea.rawg.data.model.gamedetail.Developer
import org.sea.rawg.data.model.gamedetail.EsrbRating
import org.sea.rawg.data.model.gamedetail.GamePlatformInfo
import org.sea.rawg.data.model.gamedetail.Genre
import org.sea.rawg.data.model.gamedetail.Publisher
import org.sea.rawg.data.model.gamedetail.StoreInfo
import org.sea.rawg.data.model.gamedetail.Tag

@Serializable
data class Game(
    val id: Int = 0,
    val slug: String = "",
    val name: String = "",
    val released: String? = null,
    val tba: Boolean = false,
    val background_image: String? = null,
    val rating: Float = 0f,
    val rating_top: Int = 0,
    val ratings: List<Rating> = emptyList(),
    val ratings_count: Int = 0,
    val reviews_text_count: Int = 0,
    val added: Int = 0,
    val added_by_status: AddedByStatus? = null,
    val metacritic: Int? = null,
    val playtime: Int = 0,
    val suggestions_count: Int = 0,
    val updated: String? = null,
    val user_game: String? = null,
    val reviews_count: Int = 0,
    val saturated_color: String = "",
    val dominant_color: String = "",
    val platforms: List<GamePlatformInfo> = emptyList(),
    val stores: List<StoreInfo> = emptyList(),
    val developers: List<Developer> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val publishers: List<Publisher> = emptyList(),
    val esrb_rating: EsrbRating? = null,
    val clip: String? = null,
    val description_raw: String? = null
)