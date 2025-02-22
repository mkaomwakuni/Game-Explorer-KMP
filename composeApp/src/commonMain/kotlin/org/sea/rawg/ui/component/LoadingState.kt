package org.sea.rawg.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.sea.rawg.theme.RAWGTheme

/**
 * Loading indicator component with animated dots
 * @param modifier Modifier for styling and positioning
 * @param dotColor Color of the loading dots, defaults to primary color
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    dotColor: Color = MaterialTheme.colorScheme.primary
) {
    val spacing = RAWGTheme.spacing

    Row(
        modifier = modifier
            .padding(spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val dots = listOf(
            Triple(0f, 1.2f, 1300),
            Triple(300f, 1.2f, 1300),
            Triple(600f, 1.2f, 1300)
        )

        dots.forEach { (delayMillis, targetScale, durationMillis) ->
            LoadingDot(
                dotColor = dotColor,
                delayMillis = delayMillis.toInt(),
                targetScale = targetScale,
                durationMillis = durationMillis
            )
        }
    }
}

@Composable
private fun LoadingDot(
    dotColor: Color,
    delayMillis: Int,
    targetScale: Float,
    durationMillis: Int
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loadingAnimation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = targetScale,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = FastOutSlowInEasing,
                delayMillis = delayMillis
            ),
            repeatMode = RepeatMode.Reverse
        ), label = "scaleAnimation"
    )

    Box(
        modifier = Modifier
            .size(8.dp)
            .scale(scale)
            .background(dotColor, CircleShape)
    )
}

/**
 * Full screen loading state
 */
@Composable
fun FullScreenLoading(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            LoadingIndicator()
        }
    }
}

/**
 * Loading placeholder for content that's loading
 * @param modifier Modifier for styling and positioning
 */
@Composable
fun ContentLoadingPlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator()
    }
}
