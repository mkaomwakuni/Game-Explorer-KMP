package org.sea.rawg.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class BaseGameModel(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<Game>,
    val seo_title: String? = null,
    val seo_description: String? = null,
    val seo_keywords: String? = null,
    val seo_h1: String? = null,
    val noindex: Boolean? = null,
    val nofollow: Boolean? = null,
    val description: String? = null,
    // Using JsonElement to handle any JSON structure in filters
    val filters: Map<String, JsonElement>? = null,
    val nofollow_collections: List<String>? = null
)

// Complex filter models for reference
@Serializable
data class YearFilter(
    val from: Int,
    val to: Int,
    val filter: String? = null,
    val decade: Int? = null,
    val years: List<YearRange>? = null,
    val nofollow: Boolean? = false,
    val count: Int? = null
)

@Serializable
data class YearRange(
    val year: Int,
    val count: Int,
    val nofollow: Boolean
)