package org.sea.rawg

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import moe.tlaster.precompose.lifecycle.PreComposeActivity
import org.sea.rawg.theme.CoffeeBean
import org.sea.rawg.theme.GameExplorerTheme

class MainActivity : PreComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GameExplorerTheme {
                MainView()
            }
        }
    }
}

