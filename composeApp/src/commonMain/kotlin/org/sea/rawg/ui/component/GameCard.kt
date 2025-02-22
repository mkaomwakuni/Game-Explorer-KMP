package org.sea.rawg.ui.component

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.sea.rawg.data.model.Game
import org.sea.rawg.data.model.gamedetail.GamePlatformInfo
import org.sea.rawg.theme.CustomShapes
import org.sea.rawg.theme.RAWGColors
import org.sea.rawg.theme.RAWGTheme
import org.sea.rawg.theme.RAWGTypography

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
    val spacing = RAWGTheme.spacing

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = CustomShapes.GameCard,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // Game image
            Box {
                AsyncImage(
                    url = game.background_image ?: "",
                    contentDescription = "Cover image for ${game.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (compact) 120.dp else 180.dp),
                    shape = CustomShapes.GameCard,
                    contentScale = ContentScale.Crop
                )

                if (showRating && game.rating > 0) {
                    // Rating badge
                    val ratingColor = when {
                        game.rating >= 4.5 -> RAWGColors.Rating.Exceptional
                        game.rating >= 3.5 -> RAWGColors.Rating.Recommended
                        game.rating >= 2.5 -> RAWGColors.Rating.Meh
                        else -> RAWGColors.Rating.Skip
                    }

                    Surface(
                        modifier = Modifier
                            .padding(spacing.small)
                            .align(Alignment.TopEnd),
                        shape = CustomShapes.RatingPill,
                        color = ratingColor.copy(alpha = 0.9f)
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
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }

                // Platform tags
                Row(
                    modifier = Modifier
                        .padding(spacing.small)
                        .align(Alignment.BottomStart),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Extract platform names from the platforms list
                    val platformNames = game.platforms.map { it.platform.name }
                    platformNames.take(if (compact) 2 else 3).forEach { platformName ->
                        PlatformTag(platformName)
                    }

                    if (platformNames.size > (if (compact) 2 else 3)) {
                        PlatformTag("+${platformNames.size - (if (compact) 2 else 3)}")
                    }
                }
            }

            // Game info
            Column(
                modifier = Modifier.padding(spacing.medium)
            ) {
                // Game name
                Text(
                    text = game.name,
                    style = RAWGTypography.gameTitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(spacing.extraSmall))

                // Release date
                if (game.released != null) {
                    Text(
                        text = "Released: ${game.released}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (!compact) {
                    Spacer(modifier = Modifier.height(spacing.small))

                    // Game genres
                    if (game.genres.isNotEmpty()) {
                        Text(
                            text = game.genres.joinToString(", ") { it.name },
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
}

@Composable
fun PlatformTag(text: String) {
    Surface(
        shape = CustomShapes.PlatformTag,
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

/**
 * Skeleton loading placeholder for game card
 */
@Composable
fun GameCardSkeleton(
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val spacing = RAWGTheme.spacing

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CustomShapes.GameCard,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (compact) 120.dp else 180.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Content placeholders
            Column(
                modifier = Modifier.padding(spacing.medium)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                Spacer(modifier = Modifier.height(spacing.small))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(16.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                if (!compact) {
                    Spacer(modifier = Modifier.height(spacing.small))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(16.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
            }
        }
    }
}
