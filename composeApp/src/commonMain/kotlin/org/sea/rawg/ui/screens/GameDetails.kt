package org.sea.rawg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.sea.rawg.ui.component.AsyncImage
import moe.tlaster.precompose.navigation.Navigator
import org.sea.rawg.data.model.Game
import org.sea.rawg.data.repository.GameState
import org.sea.rawg.theme.BrownSugar
import org.sea.rawg.theme.CoffeeBean
import org.sea.rawg.theme.HighRating
import org.sea.rawg.theme.LowRating
import org.sea.rawg.theme.MediumRating
import org.sea.rawg.theme.Taupe
import org.sea.rawg.theme.Wheat
import org.sea.rawg.ui.component.ErrorState
import org.sea.rawg.ui.component.FullScreenLoading
import org.sea.rawg.ui.viewmodel.GameDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetails(navigator: Navigator, gameId: Int) {
    val viewModel = remember { GameDetailsViewModel() }
    val gameState by viewModel.gameDetails

    // Load game details initially
    LaunchedEffect(gameId) {
        viewModel.loadGameDetails(gameId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Game Details")
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.goBack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (gameState) {
                is GameState.Loading -> {
                    FullScreenLoading()
                }

                is GameState.Error -> {
                    ErrorState(
                        message = (gameState as GameState.Error).message,
                        onRetry = { viewModel.loadGameDetails(gameId) }
                    )
                }

                is GameState.Success -> {
                    val game = (gameState as GameState.Success).data
                    GameDetailsContent(game)
                }

                else -> {
                    // Fallback for null state
                    Text("No game details available")
                }
            }
        }
    }
}

@Composable
private fun GameDetailsContent(game: Game) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Game header image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            if (game.background_image != null) {
                AsyncImage(
                    url = game.background_image,
                    contentDescription = game.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                GameImagePlaceholder()
            }

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                CoffeeBean.copy(alpha = 0.9f)
                            ),
                            startY = 100f
                        )
                    )
            )

            // Title and rating
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

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
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${game.rating} / 5",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ratingColor
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "(${game.ratings_count} reviews)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Game info sections
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Release info
            InfoSection(title = "Release Information") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoItem(
                        label = "Release Date",
                        value = game.released ?: "Unknown"
                    )

                    InfoItem(
                        label = "Playtime",
                        value = "${game.playtime} hours"
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Platforms
            InfoSection(title = "Platforms") {
                LazyRow(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(game.platforms) { platformInfo ->
                        PlatformChip(name = platformInfo.platform.name)
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Genres
            InfoSection(title = "Genres") {
                LazyRow(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(game.genres) { genre ->
                        GenreChip(name = genre.name)
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Description
            InfoSection(title = "About") {
                Text(
                    text = game.description_raw ?: "No description available.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Developers and Publishers
            InfoSection(title = "Developers") {
                LazyRow(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(game.developers) { developer ->
                        DeveloperChip(name = developer.name)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            InfoSection(title = "Publishers") {
                LazyRow(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(game.publishers) { publisher ->
                        PublisherChip(name = publisher.name)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        content()
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun PlatformChip(name: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = BrownSugar.copy(alpha = 0.2f),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = BrownSugar
        )
    }
}

@Composable
private fun GenreChip(name: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Taupe.copy(alpha = 0.2f),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = Taupe
        )
    }
}

@Composable
private fun DeveloperChip(name: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CoffeeBean.copy(alpha = 0.2f),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = CoffeeBean
        )
    }
}

@Composable
private fun PublisherChip(name: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Wheat.copy(alpha = 0.5f),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = CoffeeBean
        )
    }
}

@Composable
private fun GameImagePlaceholder() {
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
            tint = Taupe.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp)
        )
    }
}