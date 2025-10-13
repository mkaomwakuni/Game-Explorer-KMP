package org.sea.rawg.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.sea.rawg.data.model.Game
import org.sea.rawg.theme.BrownSugar
import org.sea.rawg.theme.CoffeeBean
import org.sea.rawg.theme.GameCardShape
import org.sea.rawg.theme.HighRating
import org.sea.rawg.theme.LowRating
import org.sea.rawg.theme.MediumRating
import org.sea.rawg.theme.Taupe
import org.sea.rawg.theme.Wheat

@Composable
fun GameCard(
    game: Game,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp)
            .clip(GameCardShape)
            .clickable { onClick(game.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = GameCardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Game image with Kamel
            if (game.background_image != null) {
                KamelImage(
                    resource = asyncPainterResource(game.background_image),
                    contentDescription = game.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    onLoading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Wheat,
                                            BrownSugar
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gamepad,
                                contentDescription = null,
                                tint = Taupe,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    },
                    onFailure = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Wheat,
                                            BrownSugar
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gamepad,
                                contentDescription = null,
                                tint = Taupe,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                )
            } else {
                // Fallback when no image is available
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Wheat,
                                    BrownSugar
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Gamepad,
                        contentDescription = null,
                        tint = Taupe,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            // Gradient overlay for better text visibility
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                CoffeeBean.copy(alpha = 0.8f)
                            ),
                            startY = 100f
                        )
                    )
            )

            // Game info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Game Title
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Release Date
                game.released?.let { releaseDate ->
                    Text(
                        text = "Released: $releaseDate",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val ratingColor = when {
                        game.rating >= 4.0 -> HighRating
                        game.rating >= 3.0 -> MediumRating
                        else -> LowRating
                    }

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = ratingColor,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = game.rating.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = ratingColor
                    )
                }
            }
        }
    }
}