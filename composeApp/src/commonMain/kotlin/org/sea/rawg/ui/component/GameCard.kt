package org.sea.rawg.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.sea.rawg.domain.models.Game

// Constants for reuse
private val CARD_CORNER_SHAPE = RoundedCornerShape(2.dp)
private val INNER_CORNER_SHAPE = RoundedCornerShape(2.dp)
private val EXTRA_SMALL_PADDING = 4.dp
private val SMALL_PADDING = 8.dp
private val MEDIUM_PADDING = 16.dp

/**
 * Game card component for displaying game information in lists
 *
 * @param game Game data to display
 * @param onClick Callback when the card is clicked
 * @param modifier Modifier for styling and positioning
 * @param showRating Whether to display the rating
 * @param compact Whether to use compact layout
 */
@Composable
fun GameCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showRating: Boolean = true,
    compact: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = CARD_CORNER_SHAPE,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column {
            // Game image section
            Box {
                // Game cover image
                AsyncImage(
                    url = game.background_image ?: "",
                    contentDescription = "Cover image for ${game.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (compact) 120.dp else 180.dp)
                        .clip(
                            RoundedCornerShape(
                                2.dp
                            )
                        ),
                    contentScale = ContentScale.Crop,
                    placeholder = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (compact) 120.dp else 180.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                    }
                )

                // Gradient overlay for better text visibility
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.5f)
                                )
                            )
                        )
                )

                // Rating badge if enabled and available
                if (showRating && game.rating > 0) {
                    val isExceptional = game.rating >= 4.5
                    val ratingColor = when {
                        game.rating >= 4.5 -> Color(0xFF66CC33) // Exceptional
                        game.rating >= 3.5 -> Color(0xFF0066CC) // Recommended
                        game.rating >= 2.5 -> Color(0xFFFFCC33) // Meh
                        else -> Color(0xFFCC3333) // Skip
                    }

                    Surface(
                        modifier = Modifier
                            .padding(SMALL_PADDING)
                            .align(Alignment.TopEnd),
                        shape = INNER_CORNER_SHAPE,
                        color = ratingColor.copy(alpha = 0.9f),
                        border = if (isExceptional) BorderStroke(
                            1.dp,
                            Color.White.copy(alpha = 0.7f)
                        ) else null
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = game.rating.toString(),
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isExceptional) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Platform tags
                Row(
                    modifier = Modifier
                        .padding(SMALL_PADDING)
                        .align(Alignment.BottomStart),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Display limited platforms based on space
                    val platformCount = if (compact) 2 else 3
                    game.platforms.take(platformCount).forEach { platform ->
                        PlatformTag(text = platform, shape = INNER_CORNER_SHAPE)
                    }

                    // Show "+X" if there are more platforms
                    if (game.platforms.size > platformCount) {
                        PlatformTag(
                            text = "+${game.platforms.size - platformCount}",
                            shape = INNER_CORNER_SHAPE
                        )
                    }
                }
            }

            // Game info section
            Column(
                modifier = Modifier.padding(MEDIUM_PADDING)
            ) {
                // Game name
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(EXTRA_SMALL_PADDING))

                // Release date
                if (game.released != null) {
                    Text(
                        text = "Released: ${game.released}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Game genres (if not in compact mode)
                if (!compact && game.genres.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(SMALL_PADDING))
                    Text(
                        text = game.genres.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun PlatformTag(
    text: String,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp)) {
    Surface(
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

