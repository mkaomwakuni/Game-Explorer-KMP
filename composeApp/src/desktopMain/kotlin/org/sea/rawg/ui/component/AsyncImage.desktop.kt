package org.sea.rawg.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

/**
 * Desktop implementation of AsyncImage using Kamel
 */
@Composable
actual fun AsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
    placeholder: @Composable () -> Unit
) {
    // Ensure URL has protocol
    val validUrl = when {
        url.isBlank() -> ""
        url.startsWith("http://") || url.startsWith("https://") -> url
        url.startsWith("//") -> "https:$url" // Add https protocol if URL starts with //
        else -> "https://$url" // Add full https:// protocol if missing
    }

    // Debug logging
    println("AsyncImage Desktop: Original URL: $url -> Processed URL: $validUrl")

    KamelImage(
        resource = asyncPainterResource(validUrl),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
        onLoading = { placeholder() },
        onFailure = { placeholder() }
    )
}