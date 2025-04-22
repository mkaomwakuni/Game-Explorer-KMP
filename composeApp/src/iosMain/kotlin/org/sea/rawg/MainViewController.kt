package org.sea.rawg

import androidx.compose.ui.window.ComposeUIViewController
import org.sea.rawg.di.AppModule

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin
    AppModule.init()
    App()
}