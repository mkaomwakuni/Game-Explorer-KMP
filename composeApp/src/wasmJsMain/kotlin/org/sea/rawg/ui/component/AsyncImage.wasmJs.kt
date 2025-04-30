package org.sea.rawg.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.events.Event

/**
 * WasmJs implementation of AsyncImage using direct DOM image element
 */
@Composable
actual fun AsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
    placeholder: @Composable () -> Unit
) {
    // Generate a unique ID for this instance
    val imageId = remember { "async-image-${url.hashCode()}" }
    
    // Track loading state
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    
    // Normalize URL to ensure it has proper protocol
    val normalizedUrl = when {
        url.isBlank() -> ""
        url.startsWith("http://") || url.startsWith("https://") -> url.trim()
        url.startsWith("//") -> "https:${url.trim()}"
        else -> "https://${url.trim()}"
    }
    
    if (normalizedUrl.isBlank()) {
        placeholder()
        return
    }

    // Always show placeholder during loading or error
    if (isLoading || isError) {
        placeholder()
    }

    // Create a box that will position our image
    Box(
        modifier = modifier.onGloballyPositioned { coordinates ->
            // Get size and position for the image
            val width = coordinates.size.width
            val height = coordinates.size.height
            val position = coordinates.positionInWindow()

            // Get the DOM element
            val element = document.getElementById(imageId)

            if (element != null && width > 0 && height > 0) {
                // Position the element absolutely within the viewport
                val visibility = if (isLoading || isError) "hidden" else "visible"
                
                element.setAttribute("style", """
                    position: absolute;
                    left: ${position.x}px;
                    top: ${position.y}px;
                    width: ${width}px;
                    height: ${height}px;
                    background-image: url('$normalizedUrl');
                    background-size: ${contentScale.toCssBackgroundSize()};
                    background-repeat: no-repeat;
                    background-position: center;
                    z-index: 1;
                    pointer-events: none;
                    visibility: $visibility;
                """.trimIndent())
            }
        }
    ) {
        // This empty Box acts as a placeholder for layout
    }

    // Create and manage the DOM element
    DisposableEffect(normalizedUrl) {
        // Remove any existing element with this ID
        document.getElementById(imageId)?.let {
            it.parentElement?.removeChild(it)
        }

        // Create a new div element for the background image
        val div = document.createElement("div") as HTMLDivElement
        div.id = imageId

        // Create a hidden image element to track loading
        val img = document.createElement("img") as HTMLImageElement
        img.setAttribute("style", "display: none;") // Hidden image for load tracking
        img.src = normalizedUrl

        if (contentDescription != null) {
            div.setAttribute("aria-label", contentDescription)
            img.alt = contentDescription
        }

        // Track loading state
        img.addEventListener("load", { _: Event ->
            isLoading = false
            isError = false

            // Make the div visible when image loads successfully
            val element = document.getElementById(imageId)
            if (element != null) {
                element.setAttribute("style", element.getAttribute("style")?.replace("visibility: hidden", "visibility: visible") ?: "")
            }
        })

        img.addEventListener("error", { _: Event ->
            isLoading = false
            isError = true
        })

        // Add the tracking image to the div
        div.appendChild(img)

        // Add the div to the document body (most reliable placement)
        document.body?.appendChild(div)

        // Listen for window resize
        val resizeListener: (Event) -> Unit = { 
            // Force recomposition on resize to update position
            val dummy = isLoading // Read state to force recomposition
        }

        window.addEventListener("resize", resizeListener)

        // Cleanup on disposal
        onDispose {
            window.removeEventListener("resize", resizeListener)
            document.getElementById(imageId)?.let {
                it.parentElement?.removeChild(it)
            }
        }
    }
}

/**
 * Convert ContentScale to CSS background-size value
 */
private fun ContentScale.toCssBackgroundSize(): String {
    return when (this) {
        ContentScale.Crop -> "cover"
        ContentScale.FillWidth -> "100% auto"
        ContentScale.FillHeight -> "auto 100%"
        ContentScale.FillBounds -> "100% 100%"
        ContentScale.Fit -> "contain"
        ContentScale.Inside -> "contain"
        ContentScale.None -> "auto"
        else -> "cover"
    }
}