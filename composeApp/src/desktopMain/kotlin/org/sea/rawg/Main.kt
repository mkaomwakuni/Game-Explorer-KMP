package org.sea.rawg

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import moe.tlaster.precompose.PreCompose
import org.sea.rawg.theme.GameExplorerTheme

fun main() = application {
    Window(
        title = "KMP-Game Explorer",
        onCloseRequest = ::exitApplication
    ) {
        // Initialize PreCompose for desktop
        PreCompose {
            GameExplorerTheme {
                App()
            }
        }
    }
}