package org.sea.rawg.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

/**
 * WasmJs implementation of AsyncImage
 * Currently just shows placeholder as image loading is not fully supported
 */
@Composable
actual fun AsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
    placeholder: @Composable () -> Unit
) {
    // Just show placeholder for now as web image loading is limited
    placeholder()
}