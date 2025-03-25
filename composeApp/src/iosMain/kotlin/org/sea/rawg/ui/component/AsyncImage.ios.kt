package org.sea.rawg.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url

/**
 * iOS implementation of AsyncImage using Kamel
 */
@Composable
actual fun AsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
    placeholder: @Composable () -> Unit
) {
    // Simple protocol normalization for iOS
    val normalizedUrl = when {
        url.isBlank() -> ""
        url.startsWith("http://") || url.startsWith("https://") -> url.trim()
        url.startsWith("//") -> "https:${url.trim()}"
        else -> "https://${url.trim()}"
    }

    KamelImage(
        resource = asyncPainterResource(normalizedUrl),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
        onLoading = { placeholder() },
        onFailure = {
            println("AsyncImage iOS error loading: $normalizedUrl")
            placeholder()
        }
    )
}