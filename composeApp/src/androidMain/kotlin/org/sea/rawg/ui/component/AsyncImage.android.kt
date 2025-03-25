package org.sea.rawg.ui.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size

/**
 * Android implementation of AsyncImage using Coil's SubcomposeAsyncImage
 * Provides comprehensive error handling and debugging for image loading issues
 */
@Composable
actual fun AsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
    placeholder: @Composable () -> Unit
) {
    // Clean and normalize URL
    val normalizedUrl = when {
        url.isBlank() -> ""
        url.startsWith("http://") || url.startsWith("https://") -> url.trim()
        url.startsWith("//") -> "https:${url.trim()}"
        else -> "https://${url.trim()}"
    }

    // Debug logging only if URL is valid
    if (normalizedUrl.isNotBlank()) {
        Log.d("AsyncImage", "Loading: $normalizedUrl")
    }

    if (normalizedUrl.isBlank()) {
        placeholder()
    } else {
        val context = LocalContext.current
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(normalizedUrl)
                .crossfade(true)
                .size(Size.ORIGINAL)  // Request original size
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .networkCachePolicy(CachePolicy.ENABLED)
                .listener(
                    onStart = {
                        Log.d("AsyncImage", "Started loading: $normalizedUrl")
                    },
                    onSuccess = { request, result ->
                        Log.d("AsyncImage", "Successfully loaded: $normalizedUrl")
                    },
                    onError = { request, error ->
                        Log.e("AsyncImage", "Error loading: $normalizedUrl", error.throwable)
                    }
                )
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier,
            loading = {
                placeholder()
            },
            error = {
                Log.e("AsyncImage", "Showing error placeholder for: $normalizedUrl")
                placeholder()
            },
            success = {
                SubcomposeAsyncImageContent()
            }
        )
    }
}