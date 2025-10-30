package org.sea.rawg.ui.screens

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.viewmodel.koinViewModel
import org.sea.rawg.data.model.DLC
import org.sea.rawg.data.model.RedditPost
import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.Screenshot
import org.sea.rawg.presentation.models.GameState
import org.sea.rawg.ui.FullScreenLoading
import org.sea.rawg.ui.component.DeveloperChip
import org.sea.rawg.ui.component.ErrorState
import org.sea.rawg.ui.component.GenreChip
import org.sea.rawg.ui.component.PlatformChip
import org.sea.rawg.ui.component.PublisherChip
import org.sea.rawg.ui.component.SectionTitle
import org.sea.rawg.ui.component.StoreChip
import org.sea.rawg.ui.component.TagChip
import org.sea.rawg.ui.component.gamedetail.GameDetailHeader
import org.sea.rawg.ui.component.gamedetail.GameQuickStatsSection
import org.sea.rawg.ui.component.gamedetail.RedditDiscussionsSection
import org.sea.rawg.ui.screens.gamedetail.FullScreenImageViewer
import org.sea.rawg.ui.screens.gamedetail.GameDetailDLCSection
import org.sea.rawg.ui.screens.gamedetail.GameScreenshotsSection
import org.sea.rawg.ui.screens.gamedetail.GameWebsiteButtonWithSeparator
import org.sea.rawg.ui.screens.gamedetail.SimilarGamesSection
import org.sea.rawg.ui.viewmodel.GameDetailsViewModel
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetails(navigator: Navigator, gameId: Int) {
    val viewModel = koinViewModel<GameDetailsViewModel>()
    val gameState by viewModel.gameDetails.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    BackHandler {
        navigator.popBackStack()
    }

    LaunchedEffect(gameId) {
        viewModel.loadGameDetails(gameId)
    }

    var isBookmarked by remember { mutableStateOf(false) }

    val dlcs by viewModel.dlcs.collectAsState()
    val redditPosts by viewModel.redditPosts.collectAsState()
    val isDlcsLoading by viewModel.isDlcsLoading.collectAsState()
    val isRedditLoading by viewModel.isRedditLoading.collectAsState()
    val screenshots by viewModel.screenshots.collectAsState()
    val similarGames by viewModel.similarGames.collectAsState()
    val isScreenshotsLoading by viewModel.isScreenshotsLoading.collectAsState()
    val isSimilarGamesLoading by viewModel.isSimilarGamesLoading.collectAsState()

    var selectedScreenshotUrl by remember { mutableStateOf<String?>(null) }

    val uriHandler = LocalUriHandler.current

    val shareGame: () -> Unit = {
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
                        navigator.navigate("game/$gameId")
                    }
                )
            }
        }

        selectedScreenshotUrl?.let { imageUrl ->
            FullScreenImageViewer(
                imageUrl = imageUrl,
                onDismiss = { selectedScreenshotUrl = null }
            )
        }

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
        GameDetailHeader(
            game = game,
            headerParallaxEffect = headerParallaxEffect.value,
            isBookmarked = isBookmarked,
            onBackPressed = onBackPressed,
            onSharePressed = onSharePressed,
            onBookmarkToggle = onBookmarkToggle
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(400.dp))

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
                        .padding(horizontal = 4.dp)
                ) {
                    GameQuickStatsSection(game = game)

                    SectionDivider()

                    GameScreenshotsSection(
                        screenshots = screenshots,
                        onScreenshotClick = onScreenshotClick,
                        isLoading = isScreenshotsLoading
                    )

                    SectionDivider()

                    AboutSection(game.description_raw)

                    SectionDivider()

                    PlatformsSection(game.getPlatformNames())

                    SectionDivider()

                    CategoriesSection(game.getGenreNames(), game.getTagNames())

                    SectionDivider()

                    CreditsSection(game.getDeveloperNames(), game.getPublisherNames())

                    SectionDivider()

                    AdditionalInfoSection(game)

                    SectionDivider()

                    GameDetailDLCSection(
                        dlcs = dlcs,
                        isLoading = isDlcsLoading,
                        onDLCClick = { dlcId ->
                            scope.launch {
                                snackbarHostState.showSnackbar("Viewing DLC details for: $dlcId")
                            }
                        }
                    )

                    SectionDivider()

                    RedditDiscussionsSection(
                        posts = redditPosts,
                        uriHandler = uriHandler,
                        isLoading = isRedditLoading
                    )

                    SectionDivider()

                    SimilarGamesSection(
                        similarGames = similarGames,
                        onGameClick = onSimilarGameClick,
                        isLoading = isSimilarGamesLoading
                    )

                    GameWebsiteButtonWithSeparator(
                        website = game.website,
                        onOpenWebsite = onOpenWebsite
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
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

        Text(
            text = "Available on",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

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
private fun SectionDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 24.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    )
}
