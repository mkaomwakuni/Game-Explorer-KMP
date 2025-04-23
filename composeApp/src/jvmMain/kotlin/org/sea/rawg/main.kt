package org.sea.rawg

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.sea.rawg.di.AppModule

fun main() {
    // Initialize Koin
    AppModule.init()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KMP-Game Explorer",
        ) {
            App()
        }
    }
}