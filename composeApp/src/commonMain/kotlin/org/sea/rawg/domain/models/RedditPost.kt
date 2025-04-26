package org.sea.rawg.domain.models
/**
 * Domain model for Reddit post information
 */
data class RedditPost(
    val id: String,
    val name: String,
    val url: String,
    val createdUtc: String? = null,
    val subreddit: String? = null,
    val username: String? = null,
    val score: Int? = null,
    val commentsCount: Int? = null
)