package org.sea.rawg

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import moe.tlaster.precompose.PreComposeApp
import org.sea.rawg.di.AppModule
import org.sea.rawg.theme.RAWGTheme

fun main() {
    // Initialize Koin
    AppModule.init()

    application {
        Window(
            title = "KMP-Game Explorer",
            onCloseRequest = ::exitApplication
        ) {
            // Initialize PreCompose for desktop
            PreComposeApp {
                RAWGTheme {
                    App()
                }
            }
        }
    }
}