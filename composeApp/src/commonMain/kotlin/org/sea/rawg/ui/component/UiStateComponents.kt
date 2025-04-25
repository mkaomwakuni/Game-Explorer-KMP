package org.sea.rawg.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.sea.rawg.data.model.ErrorType
import org.sea.rawg.theme.RAWGTheme

/**
 * LOADING STATE COMPONENTS
 */

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
    val dotCount = 3
    val animationDuration = 1200
    val dotSize = 8.dp
    val spacing = 8.dp

    val infiniteTransition = rememberInfiniteTransition(label = "loading_dots")
    val scales = List(dotCount) { index ->
        val delay = index * 160
        infiniteTransition.animateFloat(
            initialValue = 0.6f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = animationDuration,
                    easing = FastOutSlowInEasing,
                    delayMillis = delay
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "loading_dot_$index"
        )
    }

    Row(
        modifier = modifier.padding(spacing),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until dotCount) {
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .scale(scales[i].value)
                    .background(dotColor, CircleShape)
            )
        }
    }
}

/**
 * Full screen loading state with retry option after timeout
 */
@Composable
fun LoadingState(
    message: String = "Loading...",
    onRetry: () -> Unit,
    showRetryTimeout: Long = 10000,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            LoadingIndicator()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                message,
                style = MaterialTheme.typography.bodyLarge
            )

            val showRetryButton = remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                delay(showRetryTimeout)
                showRetryButton.value = true
            }

            if (showRetryButton.value) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Taking longer than expected...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Retry")
                }
            }
        }
    }
}

/**
 * EMPTY STATE COMPONENTS
 */

/**
 * Common empty state component for reuse across screens
 */
@Composable
fun EmptyState(
    title: String,
    message: String,
    icon: ImageVector = Icons.Default.Info,
    iconTint: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
    modifier: Modifier = Modifier.fillMaxSize(),
    contentAlignment: Alignment = Alignment.Center
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = contentAlignment
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = iconTint
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

/**
 * Specialized empty state for no search results
 */
@Composable
fun EmptySearchState(
    query: String = "",
    modifier: Modifier = Modifier
) {
    val message = if (query.isNotEmpty()) {
        "No results found for \"$query\". Try different keywords."
    } else {
        "No results found. Try searching for something else."
    }

    EmptyState(
        title = "No Results",
        message = message,
        icon = Icons.Default.Search,
        modifier = modifier
    )
}

/**
 * Specialized empty state for no games
 */
@Composable
fun EmptyGamesState(
    title: String = "No games found",
    message: String = "We couldn't find any games matching your criteria.",
    modifier: Modifier = Modifier
) {
    EmptyState(
        title = title,
        message = message,
        icon = Icons.Default.SportsEsports,
        modifier = modifier
    )
}

/**
 * Specialized empty state for publishers
 */
@Composable
fun EmptyPublishersState(
    title: String = "No publishers found",
    message: String = "We couldn't find any game publishers at this time.",
    modifier: Modifier = Modifier
) {
    EmptyState(
        title = title,
        message = message,
        icon = Icons.Default.Business,
        modifier = modifier
    )
}

/**
 * ERROR STATE COMPONENTS
 */

/**
 * Error state component that displays an error message with an action button
 */
@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    errorType: ErrorType = ErrorType.GENERIC
) {
    val icon = when (errorType) {
        ErrorType.NETWORK -> Icons.Default.CloudOff
        ErrorType.SERVER -> Icons.Default.Error
        ErrorType.GENERIC -> Icons.Default.Error
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Error",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Retry",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Try Again")
        }
    }
}

/**
 * Full screen error state
 */
@Composable
fun FullScreenError(
    message: String,
    onRetry: () -> Unit,
    errorType: ErrorType = ErrorType.GENERIC
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            ErrorState(
                message = message,
                onRetry = onRetry,
                errorType = errorType,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }
    }
}

/**
 * Inline error component for displaying in lists or content areas
 */
@Composable
fun InlineError(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = 8.dp

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f)
            )

            TextButton(onClick = onRetry) {
                Text(text = "Retry")
            }
        }
    }
}