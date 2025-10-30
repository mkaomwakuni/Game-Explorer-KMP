package org.sea.rawg.ui.component.gamedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sea.rawg.domain.models.Game
import org.sea.rawg.ui.component.SectionTitle
import org.sea.rawg.utils.DateUtils

/**
 * Quick stats section showing rating and playtime
 */
@Composable
fun GameQuickStatsSection(game: Game) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(title = "Quick Stats")

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Rating Card
            NeuomorphicStatCard(
                title = "Rating",
                value = "${game.rating}/5",
                icon = Icons.Default.Star,
                backgroundColor = MaterialTheme.colorScheme.primary,
                iconTint = Color(0xFFFFC107),  // Gold color for stars
                modifier = Modifier.weight(1f)
            )

            // PC Release Date Card
            GlassmorphicStatCard(
                title = "PC Release",
                value = game.released?.let { DateUtils.formatReleaseDate(it) } ?: "TBA",
                icon = Icons.Default.Computer,
                backgroundColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Playtime Card
        NeoBrutalismStatCard(
            title = "Playtime",
            value = "${game.playtime} hours",
            icon = Icons.Default.Schedule,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            accentColor = MaterialTheme.colorScheme.tertiary
        )
    }
}

/**
 * A card with a neumorphic design (soft shadows, embossed look)
 */
@Composable
fun NeuomorphicStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = backgroundColor.copy(alpha = 0.5f),
                ambientColor = backgroundColor.copy(alpha = 0.25f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor.copy(alpha = 0.15f))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(backgroundColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = backgroundColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * A card with a glassmorphic design (glass-like, semi-transparent)
 */
@Composable
fun GlassmorphicStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor.copy(alpha = 0.2f),
                        backgroundColor.copy(alpha = 0.1f)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(backgroundColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = backgroundColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = backgroundColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * A card with a neo-brutalism design (bold, high contrast, thick borders)
 */
@Composable
fun NeoBrutalismStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Color.Black.copy(alpha = 0.25f),
                clip = false
            )
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Left side content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Right side icon in a striking style
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .offset(x = 4.dp, y = (-2).dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = Color.Black.copy(alpha = 0.3f),
                        clip = false
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

/**
 * Legacy modern card design (kept for reference)
 */
@Composable
private fun ModernStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(colors = gradientColors)
            )
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Icon with circular background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}