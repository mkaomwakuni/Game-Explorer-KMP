package org.sea.rawg.theme

import androidx.compose.runtime.Composable

/**
 * iOS implementation of system UI controller
 * iOS status bar is controlled through UIKit directly, not through Compose
 */
@Composable
actual fun SystemUiControllerProvider(content: @Composable () -> Unit) {
    // iOS implementation would require platform-specific code
    // For now, just pass through
    content()
}