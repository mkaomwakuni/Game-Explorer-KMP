package org.sea.rawg

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import moe.tlaster.precompose.PreComposeApp
import org.sea.rawg.di.AppModule
import org.sea.rawg.theme.RAWGTheme

@OptIn(ExperimentalComposeUiApi::class)
@Suppress("DEPRECATION")
fun main() {
    // Initialize Koin
    AppModule.init()

    // Use CanvasBasedWindow despite deprecation warnings - this is a temporary solution
    // until we properly implement the newer APIs
    CanvasBasedWindow("KMP-Game Explorer") {
        // Initialize PreCompose for web
        PreComposeApp {
            RAWGTheme {
                App()
            }
        }
    }
}