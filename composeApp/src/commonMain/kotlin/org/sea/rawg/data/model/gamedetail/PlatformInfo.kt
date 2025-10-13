package org.sea.rawg.data.model.gamedetail

import kotlinx.serialization.Serializable

@Serializable
data class GamePlatformInfo(
    val platform: Platform = Platform(),
    val released_at: String? = null,
    val requirements_en: Requirements? = null,
    val requirements_ru: Requirements? = null
)