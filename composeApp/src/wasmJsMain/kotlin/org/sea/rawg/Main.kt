package org.sea.rawg

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import moe.tlaster.precompose.PreCompose
import org.sea.rawg.theme.GameExplorerTheme

@OptIn(ExperimentalComposeUiApi::class)
@Suppress("DEPRECATION")
fun main() {
    // Use CanvasBasedWindow despite deprecation warnings - this is a temporary solution
    // until we properly implement the newer APIs
    CanvasBasedWindow("KMP-Game Explorer") {
        // Initialize PreCompose for web
        PreCompose {
            GameExplorerTheme {
                App()
            }
        }
    }
}