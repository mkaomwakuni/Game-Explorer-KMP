package org.sea.rawg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator
import org.sea.rawg.domain.models.Game
import org.sea.rawg.navigation.NavigationRoutes
import org.sea.rawg.ui.component.AsyncImage
import org.sea.rawg.ui.component.ErrorState
import org.sea.rawg.ui.component.PlatformChip
import org.sea.rawg.ui.viewmodel.UpcomingGamesViewModel

/**
 * Screen that displays upcoming games with filtering and grouping options
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingGamesScreen(
    navigator: Navigator
) {
    val viewModel = remember { UpcomingGamesViewModel() }

    // State
    val gamesState by viewModel.gamesState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Upcoming Games",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "Releasing soon",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        },
        floatingActionButton = {
            // Show FAB only when not at the top
            val showScrollToTopButton by remember {
                derivedStateOf { gridState.firstVisibleItemIndex > 0 }
            }

            if (showScrollToTopButton) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            gridState.animateScrollToItem(0)
                        }
                    },
                    content = {
                        Icon(Icons.Default.KeyboardArrowUp, "Scroll to top")
                    }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = gamesState) {
                is UpcomingGamesViewModel.UpcomingGamesState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UpcomingGamesViewModel.UpcomingGamesState.LoadingMore -> {
                    // Show existing games with loading indicator
                    if (state.currentGames.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        UpcomingGamesGrid(
                            games = state.currentGames,
                            padding = padding,
                            gridState = gridState,
                            onGameClick = { gameId ->
                                navigator.navigate(
                                    NavigationRoutes.GAME_DETAILS.replace(
                                        "{gameId}",
                                        gameId.toString()
                                    )
                                )
                            },
                            isLoading = true,
                            onLoadMore = {}
                        )
                    }
                }

                is UpcomingGamesViewModel.UpcomingGamesState.Success -> {
                    if (state.games.isEmpty()) {
                        EmptyGamesState()
                    } else {
                        UpcomingGamesGrid(
                            games = state.games,
                            padding = padding,
                            gridState = gridState,
                            onGameClick = { gameId ->
                                navigator.navigate(
                                    NavigationRoutes.GAME_DETAILS.replace(
                                        "{gameId}",
                                        gameId.toString()
                                    )
                                )
                            },
                            isLoading = false,
                            onLoadMore = {
                                viewModel.loadMoreUpcomingGames()
                            }
                        )
                    }
                }

                is UpcomingGamesViewModel.UpcomingGamesState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.loadUpcomingGames() }
                    )
                }
            }
        }
    }
}

/**
 * Grid display of upcoming games with month headers
 */
@Composable
fun UpcomingGamesGrid(
    games: List<Game>,
    padding: PaddingValues,
    gridState: androidx.compose.foundation.lazy.grid.LazyGridState,
    onGameClick: (Int) -> Unit,
    isLoading: Boolean,
    onLoadMore: () -> Unit
) {
    // Group games by release month
    val gamesByMonth = games.groupBy { game ->
        game.released?.let { releaseDateStr ->
            try {
                // Simple parsing of YYYY-MM-DD format
                val parts = releaseDateStr.split("-")
                if (parts.size == 3) {
                    val year = parts[0].toInt()
                    val month = parts[1].toInt()
                    "${getMonthName(month)} $year"
                } else {
                    "Unknown"
                }
            } catch (e: Exception) {
                "Unknown"
            }
        } ?: "Unknown"
    }.entries.sortedBy { it.key }

    Column(modifier = Modifier.padding(padding)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(16.dp),
            state = gridState,
            modifier = Modifier.fillMaxSize()
        ) {
            // For each month group
            for (entry in gamesByMonth) {
                val month = entry.key
                val monthGames = entry.value

                item(span = { GridItemSpan(maxLineSpan) }) {
                    MonthHeader(month)
                }

                // Display games for this month
                for (game in monthGames) {
                    item {
                        UpcomingGameCard(
                            game = game,
                            onClick = { onGameClick(game.id) }
                        )
                    }
                }
            }

            // Add pagination loading indicator at the bottom
            item(span = { GridItemSpan(maxLineSpan) }) {
                // Check if we're near the end of the list to load more
                val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItems = gridState.layoutInfo.totalItemsCount

                if (lastVisibleItem >= totalItems - 5 && games.isNotEmpty() && !isLoading) {
                    LaunchedEffect(lastVisibleItem) {
                        onLoadMore()
                    }
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(36.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MonthHeader(month: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(2.dp)
    ) {
        Text(
            text = month,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun UpcomingGameCard(
    game: Game,
    onClick: () -> Unit
) {
    val cornerRadius = 2.dp
    val scale = remember { androidx.compose.animation.core.Animatable(1f) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .padding(8.dp)
            .clickable { onClick() }
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            },
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Game image
            AsyncImage(
                url = game.background_image ?: "",
                contentDescription = game.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = cornerRadius,
                            topEnd = cornerRadius,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    ),
                contentScale = ContentScale.Crop
            )

            // Release date badge
            game.released?.let { releaseDate ->
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Text(
                        text = formatReleaseDate(releaseDate),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Game info overlay
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp)
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Platforms row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    game.platforms?.take(3)?.forEach { platformResponse ->
                        platformResponse.platform?.let { platform ->
                            PlatformChip(name = platform.name)
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }

                    // Show +X more if there are more platforms
                    if ((game.platforms?.size ?: 0) > 3) {
                        Text(
                            "+${(game.platforms?.size ?: 0) - 3}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Empty state when no games are found
 */
@Composable
fun EmptyGamesState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "No upcoming games found",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Check back later for upcoming game releases",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

// Helper functions
private fun getMonthName(month: Int): String {
    return when (month) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> "Unknown"
    }
}

private fun formatReleaseDate(dateString: String): String {
    return try {
        // Simple parsing of YYYY-MM-DD format
        val parts = dateString.split("-")
        if (parts.size == 3) {
            val month = getMonthName(parts[1].toInt()).substring(0, 3)
            val day = parts[2].toInt().toString()
            "$month $day"
        } else {
            dateString
        }
    } catch (e: Exception) {
        dateString
    }
}