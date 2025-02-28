package org.sea.rawg.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class PlatformInfo(
    val platform: Platform = Platform(),
    val released_at: String? = null,
    val requirements_en: Requirements? = null,
    val requirements_ru: Requirements? = null
)