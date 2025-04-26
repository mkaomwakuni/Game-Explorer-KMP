package org.sea.rawg

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import org.sea.rawg.di.AppModule
import org.sea.rawg.theme.RAWGTheme
import org.sea.rawg.ui.screens.DesktopGamesScreen
import org.sea.rawg.ui.screens.GameDetails

fun main() {
    try {
        AppModule.init()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    application {
        val windowState = WindowState(width = 1200.dp, height = 800.dp)
        var errorMessage by remember { mutableStateOf<String?>(null) }

        Window(
            title = "KMP-Game Explorer",
            onCloseRequest = ::exitApplication,
            state = windowState
        ) {
            RAWGTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (errorMessage != null) {
                        ErrorScreen(errorMessage!!)
                    } else {
                        PreComposeApp {
                            LaunchedEffect(Unit) {
                                try {
                                    // Empty block kept for possible future initialization
                                } catch (e: Exception) {
                                    errorMessage = e.message ?: "Unknown error occurred"
                                }
                            }

                            MainContent()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Failed to load application",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Error: $message",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun MainContent() {
    val navigator = rememberNavigator()

    NavHost(
        navigator = navigator,
        initialRoute = "games"
    ) {
        scene("games") {
            DesktopGamesScreen(navigator)
        }

        scene("game/{gameId}") { backStackEntry ->
            val gameId = backStackEntry.pathMap["gameId"]?.toIntOrNull() ?: 0
            GameDetails(navigator, gameId)
        }

        scene("collection/{collectionId}") { backStackEntry ->
            val collectionId = backStackEntry.pathMap["collectionId"] ?: ""
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Collection Details: $collectionId")
            }
        }

        scene("genre/{genreId}") { backStackEntry ->
            val genreId = backStackEntry.pathMap["genreId"]?.toIntOrNull() ?: 0
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Genre Games: $genreId")
            }
        }

        scene("publisher/{publisherId}") { backStackEntry ->
            val publisherId = backStackEntry.pathMap["publisherId"]?.toIntOrNull() ?: 0
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Publisher Games: $publisherId")
            }
        }
    }
}