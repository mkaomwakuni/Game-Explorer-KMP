package org.sea.rawg.theme

import androidx.compose.runtime.Composable

/**
 * Web implementation of system UI controller
 * Web doesn't have a status bar in the same way as mobile
 */
@Composable
actual fun SystemUiControllerProvider(content: @Composable () -> Unit) {
    // No-op for web
    content()
}