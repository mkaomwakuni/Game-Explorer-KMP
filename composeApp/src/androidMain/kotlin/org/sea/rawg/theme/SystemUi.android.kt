package org.sea.rawg.theme

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
actual fun SystemUiControllerProvider(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val activity = remember(context) { context as? ComponentActivity }
    val window = activity?.window

    // Get colors from MaterialTheme
    val colorScheme = MaterialTheme.colorScheme
    val statusBarColor = colorScheme.surface
    val navigationBarColor = colorScheme.surface

    // Determine icon color based on background luminance
    val statusBarDarkIcons = statusBarColor.luminance() > 0.5f
    val navigationBarDarkIcons = navigationBarColor.luminance() > 0.5f

    // Apply system UI colors
    DisposableEffect(activity, statusBarColor, navigationBarColor) {
        activity?.window?.let { window ->
            // Set colors
            window.statusBarColor = statusBarColor.toArgb()
            window.navigationBarColor = navigationBarColor.toArgb()

            // Configure insets controller for light/dark icons
            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = statusBarDarkIcons
                isAppearanceLightNavigationBars = navigationBarDarkIcons

                // Prevent back gesture from directly exiting the app
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            // Make system UI work better with edge-to-edge design
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

        onDispose { }
    }

    content()
}