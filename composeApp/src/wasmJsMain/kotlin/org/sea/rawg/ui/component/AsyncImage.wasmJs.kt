package org.sea.rawg.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntSize
import kotlinx.browser.document
import kotlinx.browser.window
import org.sea.rawg.theme.placeholder
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.events.EventListener
import kotlin.math.roundToInt

@Composable
actual fun AsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
    placeholder: @Composable () -> Unit
) {
    val uniqueId = remember { "img_${(Math.random() * 1000000).toInt()}" }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val normalizedUrl = remember(url) {
        when {
            url.isBlank() -> ""
            url.startsWith("http://") || url.startsWith("https://") -> url.trim()
            url.startsWith("//") -> "https:${url.trim()}"
            else -> "https://${url.trim()}"
        }
    }

    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    var boxPosition by remember { mutableStateOf(androidx.compose.ui.geometry.Offset.Zero) }

    if (isLoading || normalizedUrl.isBlank() || isError) {
        Box(
            modifier = modifier.background(Color.LightGray.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            placeholder()
        }
    }

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                boxSize = coordinates.size
                boxPosition = coordinates.positionInRoot()
            },
        contentAlignment = Alignment.Center
    ) {
        DisposableEffect(normalizedUrl, boxSize, boxPosition) {
            document.getElementById(uniqueId)?.let {
                document.body?.removeChild(it)
            }

            if (normalizedUrl.isNotBlank() && boxSize.width > 0 && boxSize.height > 0) {
                val div = document.createElement("div") as HTMLDivElement
                div.id = uniqueId
                div.style.position = "absolute"
                div.style.left = "${boxPosition.x.roundToInt()}px"
                div.style.top = "${boxPosition.y.roundToInt()}px"
                div.style.width = "${boxSize.width}px"
                div.style.height = "${boxSize.height}px"
                div.style.objectFit = when (contentScale) {
                    ContentScale.Crop -> "cover"
                    ContentScale.Fit -> "contain"
                    else -> "cover"
                }
                div.style.backgroundSize = when (contentScale) {
                    ContentScale.Crop -> "cover"
                    ContentScale.Fit -> "contain"
                    else -> "cover"
                }
                div.style.backgroundPosition = "center"
                div.style.backgroundRepeat = "no-repeat"

                val img = document.createElement("img") as HTMLImageElement
                img.setAttribute("style", "display: none;")
                img.setAttribute("alt", contentDescription ?: "")
                
                val loadHandler = EventListener {
                    isLoading = false
                    div.style.backgroundImage = "url('$normalizedUrl')"
                    div.style.backgroundColor = "transparent"
                }
                
                val errorHandler = EventListener {
                    isLoading = false
                    isError = true
                    console.error("Failed to load image: $normalizedUrl")
                }
                
                img.addEventListener("load", loadHandler)
                img.addEventListener("error", errorHandler)
                img.src = normalizedUrl

                div.appendChild(img)
                document.body?.appendChild(div)
            }

            val resizeListener = {
                // Force recomposition on resize to update position
                isLoading = isLoading
            }
            
            window.addEventListener("resize", resizeListener)

            onDispose {
                document.getElementById(uniqueId)?.let {
                    document.body?.removeChild(it)
                }
                window.removeEventListener("resize", resizeListener)
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // Empty box to ensure proper layout
        }
    }
}