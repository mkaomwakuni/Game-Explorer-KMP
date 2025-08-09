package org.sea.rawg.theme

import androidx.compose.runtime.Composable

@Composable
expect fun SystemUiControllerProvider(content: @Composable () -> Unit)


interface StatusBarController {
    fun setStatusBarColor(color: androidx.compose.ui.graphics.Color, darkIcons: Boolean)
    fun setNavigationBarColor(color: androidx.compose.ui.graphics.Color, darkIcons: Boolean)
}