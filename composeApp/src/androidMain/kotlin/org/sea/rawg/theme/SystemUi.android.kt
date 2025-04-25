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

@Composable
actual fun SystemUiControllerProvider(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val activity = remember(context) { context as? ComponentActivity }

    val surface = MaterialTheme.colorScheme.surface
    val useDarkIcons = surface.luminance() > 0.5f

    DisposableEffect(activity, surface, useDarkIcons) {
        activity?.window?.let { window ->
            window.statusBarColor = surface.toArgb()
            window.navigationBarColor = surface.toArgb()

            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = useDarkIcons
                isAppearanceLightNavigationBars = useDarkIcons
            }
        }
        onDispose { }
    }

    content()
}