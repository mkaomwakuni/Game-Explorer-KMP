package org.sea.rawg.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.activity.ComponentActivity
import android.app.Activity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.luminance

/**
 * System UI controller for Android
 */
class SystemUiController(private val activity: Activity) {
    /**
     * Set the status bar color and icon appearance
     */
    fun setStatusBarColor(color: Color, darkIcons: Boolean) {
        activity.window.statusBarColor = color.hashCode()
        WindowCompat.getInsetsController(activity.window, activity.window.decorView).apply {
            isAppearanceLightStatusBars = darkIcons
        }
    }

    /**
     * Set the navigation bar color and icon appearance
     */
    fun setNavigationBarColor(color: Color, darkIcons: Boolean) {
        activity.window.navigationBarColor = color.hashCode()
        WindowCompat.getInsetsController(activity.window, activity.window.decorView).apply {
            isAppearanceLightNavigationBars = darkIcons
        }
    }
}

/**
 * Composition local provider for the system UI controller
 */
val LocalSystemUiController = staticCompositionLocalOf<SystemUiController?> { null }

/**
 * Provides the system UI controller
 */
@Composable
actual fun SystemUiControllerProvider(content: @Composable () -> Unit) {
    // Get current context and safely cast to Activity
    val context = LocalContext.current
    val activity = remember {
        when (context) {
            is ComponentActivity -> context
            else -> null
        }
    }

    val controller = remember(activity) {
        activity?.let { SystemUiController(it) }
    }

    // Get theme colors
    val colorScheme = MaterialTheme.colorScheme
    val isDarkTheme = colorScheme.background.luminance() < 0.5f

    // Effect to update system bars when the theme changes
    DisposableEffect(controller, isDarkTheme) {
        controller?.setStatusBarColor(
            color = colorScheme.surface,
            darkIcons = !isDarkTheme // Light icons on dark theme, dark icons on light theme
        )
        controller?.setNavigationBarColor(
            color = colorScheme.surface,
            darkIcons = !isDarkTheme
        )
        onDispose { }
    }

    CompositionLocalProvider(LocalSystemUiController provides controller) {
        content()
    }
}