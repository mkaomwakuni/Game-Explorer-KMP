package org.sea.rawg

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import moe.tlaster.precompose.PreComposeApp
import org.sea.rawg.di.AppModule
import org.sea.rawg.theme.RAWGTheme

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // Initialize Koin
    AppModule.init()

    // Using ComposeViewport for web target
    ComposeViewport("ComposeTarget") {
        // Initialize PreCompose for web
        PreComposeApp {
            RAWGTheme {
                App()
            }
        }
    }
}