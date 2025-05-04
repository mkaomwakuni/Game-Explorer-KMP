package org.sea.rawg.ui.screens.gamedetail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.viewmodel.koinViewModel
import org.sea.rawg.data.model.DLC
import org.sea.rawg.data.model.RedditPost
import org.sea.rawg.domain.models.Game
import org.sea.rawg.presentation.models.GameState
import org.sea.rawg.ui.FullScreenLoading
import org.sea.rawg.ui.component.AsyncImage
import org.sea.rawg.ui.component.ErrorState
import org.sea.rawg.ui.component.SectionTitle
import org.sea.rawg.ui.component.gamedetail.GameDetailHeader
import org.sea.rawg.ui.component.gamedetail.GameRatingBar
import org.sea.rawg.ui.component.gamedetail.RedditDiscussionsSection
import org.sea.rawg.ui.screens.gamedetail.GameDetailDLCSection
import org.sea.rawg.ui.viewmodel.GameDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(navigator: Navigator, gameId: Int) {
    val viewModel = koinViewModel<GameDetailsViewModel>()
    val gameState by viewModel.gameDetails.collectAsState()
    val dlcState by viewModel.dlcs.collectAsState()
    val redditState by viewModel.redditPosts.collectAsState()
    val isDlcLoading by viewModel.isDlcsLoading.collectAsState()
    val isRedditLoading by viewModel.isRedditLoading.collectAsState()
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

    // Track if the game is bookmarked
    var isBookmarked by remember { mutableStateOf(false) }

    // Handle UI states
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
                ModernGameDetailContent(
                    game = game,
                    navigator = navigator,
                    isBookmarked = isBookmarked,
                    onBookmarkToggle = {
                        isBookmarked = !isBookmarked
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = if (isBookmarked) "${game.name} added to favorites"
                                else "${game.name} removed from favorites"
                            )
                        }
                    },
                    dlcs = dlcState,
                    redditPosts = redditState,
                    isDlcLoading = isDlcLoading,
                    isRedditLoading = isRedditLoading
                )
            }
        }

        // Snackbar host
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
fun ModernGameDetailContent(
    game: Game,
    navigator: Navigator,
    isBookmarked: Boolean,
    onBookmarkToggle: () -> Unit,
    dlcs: List<DLC>?,
    redditPosts: List<RedditPost>?,
    isDlcLoading: Boolean,
    isRedditLoading: Boolean
) {
    val scrollState = rememberScrollState()
    val headerHeight = 400.dp
    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
    val scrollProgress = derivedStateOf { (scrollState.value / headerHeightPx).coerceIn(0f, 1f) }
    val uriHandler = LocalUriHandler.current

    // Track expanded description state
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 24.dp)
        ) {
            // Header placeholder
            Spacer(modifier = Modifier.height(headerHeight - 40.dp))

            // Main content card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // Game title and meta information (overlapping with the header)
                    Column(
                        modifier = Modifier
                            .offset(y = (-40).dp)
                            .fillMaxWidth()
                    ) {
                        // Title section with drop shadow
                        Text(
                            text = game.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Release date
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = game.released ?: "Release date unknown",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Ratings
                        if (game.rating > 0) {
                            GameRatingBar(
                                rating = game.rating,
                                reviewCount = game.ratings_count
                            )
                        }
                    }

                    // Platform chips in horizontal scrollable row
                    val platforms = game.getPlatformNames()
                    if (platforms.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(platforms) { platform ->
                                Surface(
                                    shape = RoundedCornerShape(2.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Text(
                                        text = platform,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 6.dp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // About section with expandable text
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SectionTitle(title = "About")

                        Spacer(modifier = Modifier.height(8.dp))

                        game.description_raw?.let { description ->
                            if (description.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .animateContentSize()
                                        .clickable {
                                            isDescriptionExpanded = !isDescriptionExpanded
                                        }
                                ) {
                                    Text(
                                        text = description,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 4,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    if (!isDescriptionExpanded && description.length > 200) {
                                        TextButton(
                                            onClick = { isDescriptionExpanded = true },
                                            contentPadding = PaddingValues(0.dp)
                                        ) {
                                            Text("Read more")
                                        }
                                    } else if (isDescriptionExpanded) {
                                        TextButton(
                                            onClick = { isDescriptionExpanded = false },
                                            contentPadding = PaddingValues(0.dp)
                                        ) {
                                            Text("Show less")
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = "No description available",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider()

                    // DLC Section
                    GameDetailDLCSection(
                        dlcs = dlcs,
                        isLoading = isDlcLoading
                    )

                    HorizontalDivider()

                    // Reddit discussions section
                    RedditDiscussionsSection(
                        posts = redditPosts ?: emptyList(),
                        uriHandler = uriHandler,
                        isLoading = isRedditLoading
                    )

                    // Genres and Tags
                    if (!game.genres.isNullOrEmpty() || !game.tags.isNullOrEmpty()) {
                        HorizontalDivider()

                        // Genres
                        if (!game.genres.isNullOrEmpty()) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                SectionTitle(title = "Genres")

                                Spacer(modifier = Modifier.height(8.dp))

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    game.genres!!.forEach { genre ->
                                        Surface(
                                            shape = RoundedCornerShape(2.dp),
                                            color = MaterialTheme.colorScheme.tertiaryContainer.copy(
                                                alpha = 0.7f
                                            )
                                        ) {
                                            Text(
                                                text = genre.name,
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                                modifier = Modifier.padding(
                                                    horizontal = 12.dp,
                                                    vertical = 6.dp
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Tags (limited to 8 to avoid cluttering)
                        if (!game.tags.isNullOrEmpty()) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                SectionTitle(title = "Tags", count = game.tags!!.size)

                                Spacer(modifier = Modifier.height(8.dp))

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    game.tags!!.take(8).forEach { tag ->
                                        Surface(
                                            shape = RoundedCornerShape(2.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant
                                        ) {
                                            Text(
                                                text = tag.name,
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(
                                                    horizontal = 12.dp,
                                                    vertical = 6.dp
                                                )
                                            )
                                        }
                                    }

                                    // Show "+X more" if there are more tags
                                    if (game.tags!!.size > 8) {
                                        Surface(
                                            shape = RoundedCornerShape(2.dp),
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                        ) {
                                            Text(
                                                text = "+${game.tags!!.size - 8} more",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(
                                                    horizontal = 12.dp,
                                                    vertical = 6.dp
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Fixed parallax header at the top
        GameDetailHeader(
            game = game,
            headerParallaxEffect = scrollProgress.value,
            isBookmarked = isBookmarked,
            onBackPressed = { navigator.popBackStack() },
            onSharePressed = { /* Implement share action */ },
            onBookmarkToggle = onBookmarkToggle
        )
    }
}

@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val horizontalSpacer = horizontalArrangement.spacing.roundToPx()
        val verticalSpacer = verticalArrangement.spacing.roundToPx()

        // Measure and place children
        val placeables =
            measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }

        // Track position of each row
        val rows = mutableListOf<Int>()
        var rowMaxHeight = 0
        var rowWidth = 0
        var totalHeight = 0

        placeables.forEachIndexed { index, placeable ->
            // Check if we need to move to the next row
            if (index > 0 && rowWidth + placeable.width + horizontalSpacer > constraints.maxWidth) {
                totalHeight += rowMaxHeight + verticalSpacer
                rows.add(rowMaxHeight)
                rowMaxHeight = 0
                rowWidth = 0
            }

            rowWidth += placeable.width + if (rowWidth > 0) horizontalSpacer else 0
            rowMaxHeight = maxOf(rowMaxHeight, placeable.height)

            // Handle the last row
            if (index == placeables.lastIndex) {
                rows.add(rowMaxHeight)
                totalHeight += rowMaxHeight
            }
        }

        // Set the layout's dimensions
        layout(
            width = constraints.maxWidth,
            height = totalHeight
        ) {
            var x = 0
            var y = 0
            var rowIndex = 0
            var rowItemCount = 0

            placeables.forEach { placeable ->
                // Check if we need to move to the next row
                if (x + placeable.width > constraints.maxWidth) {
                    x = 0
                    y += rows[rowIndex] + verticalSpacer
                    rowIndex++
                    rowItemCount = 0
                }

                // Add horizontal spacing if not the first item in the row
                if (rowItemCount > 0) {
                    x += horizontalSpacer
                }

                placeable.placeRelative(x, y)
                x += placeable.width
                rowItemCount++
            }
        }
    }
}

@Composable
fun HorizontalDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 24.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    )
}