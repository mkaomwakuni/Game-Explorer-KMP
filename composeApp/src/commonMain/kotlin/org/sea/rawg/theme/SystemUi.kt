package org.sea.rawg.theme

import androidx.compose.runtime.Composable

/**
 * Expect declaration for system UI controller provider
 * Each platform will provide its own implementation
 */
@Composable
expect fun SystemUiControllerProvider(content: @Composable () -> Unit)

/**
 * Interface for status bar configuration
 */
interface StatusBarController {
    fun setStatusBarColor(color: androidx.compose.ui.graphics.Color, darkIcons: Boolean)
    fun setNavigationBarColor(color: androidx.compose.ui.graphics.Color, darkIcons: Boolean)
}