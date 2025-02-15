package org.sea.rawg

import android.os.Build
import androidx.compose.runtime.Composable
import moe.tlaster.precompose.PreComposeApp

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()


@Composable
fun MainView() {
    PreComposeApp {
        App()
    }
}