package org.sea.rawg.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.Navigator
import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.Screenshot
import org.sea.rawg.presentation.models.GameState
import org.sea.rawg.ui.FullScreenLoading
import org.sea.rawg.ui.component.AsyncImage
import org.sea.rawg.ui.component.DeveloperChip
import org.sea.rawg.ui.component.ErrorState
import org.sea.rawg.ui.component.GenreChip
import org.sea.rawg.ui.component.PlatformChip
import org.sea.rawg.ui.component.PublisherChip
import org.sea.rawg.ui.component.StoreChip
import org.sea.rawg.ui.component.TagChip
import org.sea.rawg.ui.viewmodel.GameDetailsViewModel
import kotlin.math.min
import org.sea.rawg.data.model.DLC
import org.sea.rawg.data.model.RedditPost
import org.sea.rawg.ui.component.gamedetail.DLCSection
import org.sea.rawg.ui.component.gamedetail.RedditDiscussionsSection
import org.sea.rawg.ui.component.SectionTitle
import org.sea.rawg.ui.component.gamedetail.GameDetailHeader
import org.sea.rawg.ui.component.gamedetail.GameRatingBar
import org.sea.rawg.ui.component.gamedetail.GameQuickStatsSection
import org.sea.rawg.ui.screens.gamedetail.GameDetailDLCSection
import org.sea.rawg.ui.screens.gamedetail.GameScreenshotsSection
import org.sea.rawg.ui.screens.gamedetail.SimilarGamesSection
import org.sea.rawg.ui.screens.gamedetail.GameWebsiteButtonWithSeparator
import org.sea.rawg.ui.screens.gamedetail.FullScreenImageViewer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetails(navigator: Navigator, gameId: Int) {
    val viewModel = remember { GameDetailsViewModel() }
    val gameState by viewModel.gameDetails.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Handle back button press
    BackHandler {
        navigator.popBackStack()
    }

    // Load game details initially
    LaunchedEffect(gameId) {
        viewModel.loadGameDetails(gameId)
    }

    // Track if the game is bookmarked (would be connected to a repository in a real app)
    var isBookmarked by remember { mutableStateOf(false) }

    // Get DLCs and Reddit posts from ViewModel
    val dlcs by viewModel.dlcs.collectAsState()
    val redditPosts by viewModel.redditPosts.collectAsState()
    val isDlcsLoading by viewModel.isDlcsLoading.collectAsState()
    val isRedditLoading by viewModel.isRedditLoading.collectAsState()
    val screenshots by viewModel.screenshots.collectAsState()
    val similarGames by viewModel.similarGames.collectAsState()
    val isScreenshotsLoading by viewModel.isScreenshotsLoading.collectAsState()
    val isSimilarGamesLoading by viewModel.isSimilarGamesLoading.collectAsState()

    // State for full screen screenshot viewer
    var selectedScreenshotUrl by remember { mutableStateOf<String?>(null) }

    // Get the URI handler
    val uriHandler = LocalUriHandler.current

    // Share action handler
    val shareGame: () -> Unit = {
        // In a real implementation, this would share the game details
        scope.launch {
            snackbarHostState.showSnackbar("Sharing game details...")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                // Debug print to verify website URL
                println("Game website URL: ${game.website}")
                GameDetailsContent(
                    game = game,
                    isBookmarked = isBookmarked,
                    dlcs = dlcs,
                    redditPosts = redditPosts,
                    screenshots = screenshots,
                    similarGames = similarGames,
                    isDlcsLoading = isDlcsLoading,
                    isRedditLoading = isRedditLoading,
                    isScreenshotsLoading = isScreenshotsLoading,
                    isSimilarGamesLoading = isSimilarGamesLoading,
                    onBackPressed = { navigator.popBackStack() },
                    onSharePressed = shareGame,
                    onOpenWebsite = { url ->
                        uriHandler.openUri(url)
                    },
                    onBookmarkToggle = {
                        isBookmarked = !isBookmarked
                        // Show a snackbar when bookmarking/unbookmarking
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = if (isBookmarked) "${game.name} added to favorites"
                                else "${game.name} removed from favorites"
                            )
                        }
                    },
                    onScreenshotClick = { imageUrl ->
                        selectedScreenshotUrl = imageUrl
                    },
                    onSimilarGameClick = { gameId ->
                        // Navigate to the selected game detail screen
                        navigator.navigate("game/$gameId")
                    }
                )
            }
        }

        // Full screen image viewer for screenshots
        selectedScreenshotUrl?.let { imageUrl ->
            FullScreenImageViewer(
                imageUrl = imageUrl,
                onDismiss = { selectedScreenshotUrl = null }
            )
        }

        // Snackbar host at the bottom with proper system bars padding
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .systemBarsPadding()
                .padding(16.dp)
        )
    }
}

@Composable
private fun GameDetailsContent(
    game: Game,
    isBookmarked: Boolean,
    dlcs: List<DLC>,
    redditPosts: List<RedditPost>,
    screenshots: List<Screenshot>,
    similarGames: List<Game>,
    isDlcsLoading: Boolean,
    isRedditLoading: Boolean,
    isScreenshotsLoading: Boolean,
    isSimilarGamesLoading: Boolean,
    onBackPressed: () -> Unit,
    onSharePressed: () -> Unit,
    onOpenWebsite: (String) -> Unit,
    onBookmarkToggle: () -> Unit,
    onScreenshotClick: (String) -> Unit,
    onSimilarGameClick: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val scrollProgress = remember { derivedStateOf { scrollState.value.toFloat() / 400f } }
    val headerParallaxEffect = animateFloatAsState(targetValue = min(scrollProgress.value, 1f))
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val uriHandler = LocalUriHandler.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Game header with parallax effect
        GameDetailHeader(
            game = game,
            headerParallaxEffect = headerParallaxEffect.value,
            isBookmarked = isBookmarked,
            onBackPressed = onBackPressed,
            onSharePressed = onSharePressed,
            onBookmarkToggle = onBookmarkToggle
        )

        // Main scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Empty space for the header
            Spacer(modifier = Modifier.height(400.dp))

            // Custom hero section divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(50)
                        )
                )
            }

            // Main content card (slightly elevated above the background)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Quick stats
                    GameQuickStatsSection(game)

                    SectionDivider()

                    // Screenshots Section
                    GameScreenshotsSection(
                        screenshots = screenshots,
                        onScreenshotClick = onScreenshotClick,
                        isLoading = isScreenshotsLoading
                    )

                    SectionDivider()

                    // About section (description)
                    AboutSection(game.description_raw)

                    SectionDivider()

                    // Platforms section - using getPlatformNames() from Game model
                    PlatformsSection(game.getPlatformNames())

                    SectionDivider()

                    // Genres and tags in a grid - using getGenreNames() and getTagNames() from Game model
                    CategoriesSection(game.getGenreNames(), game.getTagNames())

                    SectionDivider()

                    // Credits section (developers and publishers) - using getDeveloperNames() and getPublisherNames() from Game model
                    CreditsSection(game.getDeveloperNames(), game.getPublisherNames())

                    SectionDivider()

                    // Additional info
                    AdditionalInfoSection(game)

                    SectionDivider()

                    // DLC Section
                    GameDetailDLCSection(
                        dlcs = dlcs,
                        isLoading = isDlcsLoading,
                        onDLCClick = { dlcId ->
                            // In a real implementation, this would navigate to DLC details
                            scope.launch {
                                snackbarHostState.showSnackbar("Viewing DLC details for: $dlcId")
                            }
                        }
                    )

                    SectionDivider()

                    // Reddit Discussions Section
                    RedditDiscussionsSection(
                        posts = redditPosts,
                        uriHandler = uriHandler,
                        isLoading = isRedditLoading
                    )

                    SectionDivider()

                    // Similar Games Section
                    SimilarGamesSection(
                        similarGames = similarGames,
                        onGameClick = onSimilarGameClick,
                        isLoading = isSimilarGamesLoading
                    )

                    // Website Button
                    GameWebsiteButtonWithSeparator(
                        website = game.website,
                        onOpenWebsite = onOpenWebsite
                    )

                    // Bottom space
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
private fun RatingBar(rating: Float, reviewCount: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        // Convert rating to stars (out of 5)
        val fullStars = rating.toInt()
        val hasHalfStar = rating - fullStars >= 0.5f
        val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0

        repeat(fullStars) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFD700), // Gold color
                modifier = Modifier.size(24.dp)
            )
        }

        if (hasHalfStar) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.StarHalf,
                contentDescription = null,
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(24.dp)
            )
        }

        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Default.StarOutline,
                contentDescription = null,
                tint = Color(0xFFFFD700).copy(alpha = 0.5f),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Rating text
        Text(
            text = "$rating/5",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )

        Text(
            text = "($reviewCount reviews)",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun ScreenshotsSection(screenshots: List<String>, onScreenshotClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(title = "Screenshots")

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
        ) {
            items(screenshots) { screenshotUrl ->
                Card(
                    modifier = Modifier
                        .width(280.dp)
                        .height(160.dp)
                        .clickable { onScreenshotClick(screenshotUrl) },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    AsyncImage(
                        url = screenshotUrl,
                        contentDescription = "Screenshot",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun FullScreenImageViewer(imageUrl: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f))
            .clickable(onClick = onDismiss)
            .systemBarsPadding(),  // Add padding for system bars
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            url = imageUrl,
            contentDescription = "Full-screen image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            contentScale = ContentScale.FillWidth
        )

        // Close button
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(48.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun AboutSection(description: String?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(title = "About")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = description ?: "No description available for this game.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun PlatformsSection(platforms: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(title = "Available Platforms")

        Spacer(modifier = Modifier.height(16.dp))

        if (platforms.isEmpty()) {
            Text(
                text = "No platform information available",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(platforms) { platformName ->
                    PlatformChip(name = platformName)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoriesSection(genres: List<String>, tags: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Genres section
        SectionTitle(title = "Genres")

        Spacer(modifier = Modifier.height(16.dp))

        if (genres.isEmpty()) {
            Text(
                text = "No genre information available",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                genres.forEach { genre ->
                    GenreChip(name = genre)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tags section
        Text(
            text = "Tags",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (tags.isEmpty()) {
            Text(
                text = "No tags available",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.take(15).forEach { tag ->
                    TagChip(name = tag)
                }
            }
        }
    }
}

@Composable
private fun CreditsSection(developers: List<String>, publishers: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Developers section
        SectionTitle(title = "Development")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Developers",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (developers.isEmpty()) {
            Text(
                text = "Developer information unavailable",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(developers) { developer ->
                    DeveloperChip(name = developer)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Publishers section
        Text(
            text = "Publishers",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (publishers.isEmpty()) {
            Text(
                text = "Publisher information unavailable",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(publishers) { publisher ->
                    PublisherChip(name = publisher)
                }
            }
        }
    }
}

@Composable
private fun AdditionalInfoSection(game: Game) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(title = "Additional Information")

        Spacer(modifier = Modifier.height(16.dp))

        // Stores section
        Text(
            text = "Available on",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Use getStoreNames() from Game model
        val storeNames = game.getStoreNames()
        if (storeNames.isEmpty()) {
            Text(
                text = "Store information unavailable",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(storeNames) { storeName ->
                    StoreChip(name = storeName)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Game ID
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Game ID: ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "#${game.id}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Game slug
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Slug: ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = game.slug,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        }

       
    }
}

@Composable
private fun GameImagePlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Gamepad,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.size(80.dp)
        )
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 24.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    )
}
