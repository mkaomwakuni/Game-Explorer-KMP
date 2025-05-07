package org.sea.rawg.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RedditPost(
    val id: String? = null,
    val name: String? = null,
    val text: String? = null,
    val image: String? = null,
    val url: String? = null,
    val username: String? = null,
    val subreddit: String? = null,
    @SerialName("created")
    val createdAt: String? = null
)