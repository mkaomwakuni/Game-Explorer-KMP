package org.sea.rawg.theme

import androidx.compose.runtime.Composable

actual fun ConfigureSystemUi() {
}

@Composable
actual fun SystemUiControllerProvider(content: @Composable () -> Unit) {
    content()
}