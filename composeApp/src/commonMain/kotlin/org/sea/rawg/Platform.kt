package org.sea.rawg

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform


@Composable
expect fun App()