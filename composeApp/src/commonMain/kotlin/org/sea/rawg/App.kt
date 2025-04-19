package org.sea.rawg

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.sea.rawg.ui.screens.MainScreen

/**
 * Main application composable
 * PreCompose is initialized in platform-specific entry points (MainActivity for Android)
 */
@Composable
@Preview
fun App() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        MainScreen()
    }
}