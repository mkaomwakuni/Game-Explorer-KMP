package org.sea.rawg.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.concurrent.ConcurrentHashMap
import javax.imageio.ImageIO
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate

private val imageCache = ConcurrentHashMap<String, ImageBitmap>()

private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit
})

@Composable
actual fun AsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
    placeholder: @Composable () -> Unit
) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    if (url.isBlank()) {
        placeholder()
        return
    }

    val fixedUrl = when {
        url.startsWith("http://") || url.startsWith("https://") -> url
        url.startsWith("//") -> "https:$url"
        else -> "https://$url"
    }

    LaunchedEffect(fixedUrl) {
        val cachedImage = imageCache[fixedUrl]
        if (cachedImage != null) {
            imageBitmap = cachedImage
            isLoading = false
            return@LaunchedEffect
        }

        isLoading = true
        isError = false

        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            val bitmap = withContext(Dispatchers.IO) {
                try {
                    val connection = URL(fixedUrl).openConnection() as HttpsURLConnection
                    connection.hostnameVerifier = HostnameVerifier { _, _ -> true }
                    connection.sslSocketFactory = sslContext.socketFactory
                    connection.connectTimeout = 8000
                    connection.readTimeout = 15000
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0")

                    connection.inputStream.use { inputStream ->
                        val bufferedImage = ImageIO.read(inputStream)
                        if (bufferedImage != null) {
                            ByteArrayOutputStream().use { baos ->
                                ImageIO.write(bufferedImage, "png", baos)
                                Image.makeFromEncoded(baos.toByteArray()).toComposeImageBitmap()
                            }
                        } else null
                    }
                } catch (e: Exception) {
                    null
                } finally {
                    connection?.disconnect()
                }
            }

            if (bitmap != null) {
                imageCache[fixedUrl] = bitmap
                imageBitmap = bitmap
                isLoading = false
            } else {
                isError = true
                isLoading = false
            }
        } catch (e: Exception) {
            isError = true
            isLoading = false
        }
    }

    Box(modifier = modifier) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxSize(0.3f)
                    )
                }
            }
            imageBitmap != null -> {
                Image(
                    painter = BitmapPainter(imageBitmap!!),
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = contentScale
                )
            }
            else -> {
                placeholder()
            }
        }
    }
}