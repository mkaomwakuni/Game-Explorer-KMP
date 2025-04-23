package org.sea.rawg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import moe.tlaster.precompose.PreComposeApp
import org.sea.rawg.theme.RAWGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make system bars (status bar & navigation bar) transparent
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContent {
            PreComposeApp {
                RAWGTheme {
                    App()
                }
            }
        }
    }
}

