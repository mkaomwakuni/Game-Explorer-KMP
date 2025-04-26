package org.sea.rawg.theme

import androidx.compose.runtime.Composable

/**
 * Desktop implementation of SystemUiControllerProvider
 * For desktop, we simply render the content without any special system UI handling
 */
@Composable
actual fun SystemUiControllerProvider(content: @Composable () -> Unit) {
    // On desktop, we don't need special system UI handling
    content()
}