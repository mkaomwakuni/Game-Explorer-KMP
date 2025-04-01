package org.sea.rawg

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.rememberNavigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.sea.rawg.navigation.NavGraph

/**
 * Main application composable
 * PreCompose is initialized in platform-specific entry points (MainActivity for Android)
 */
@Composable
@Preview
fun App() {
    val navigator = rememberNavigator()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavGraph(navigator = navigator)
    }
}