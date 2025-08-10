package org.sea.rawg

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.sea.rawg.di.AppModule
import org.sea.rawg.theme.AppTheme
import org.sea.rawg.ui.MainApp

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    AppModule.init()

    CanvasBasedWindow("Game Explorer") {
        AppTheme {
            MainApp()
        }
    }
}