package org.sea.rawg.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import org.sea.rawg.domain.models.Game
import org.sea.rawg.navigation.NavigationRoutes
import org.sea.rawg.ui.component.EmptyGamesState
import org.sea.rawg.ui.component.ErrorState
import org.sea.rawg.ui.component.LoadingState
import org.sea.rawg.ui.component.cards.MonthHeader
import org.sea.rawg.ui.component.cards.UpcomingGameCard
import org.sea.rawg.ui.component.cards.getMonthName
import org.sea.rawg.ui.viewmodel.UpcomingGamesViewModel

/**
 * Screen that displays upcoming games with filtering and grouping options
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingGamesScreen(
    navigator: Navigator
) {
    val viewModel = koinInject<UpcomingGamesViewModel>()

    // State
    val gamesState = viewModel.gamesState.value
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
                    LoadingState(
                        message = "Loading upcoming games...",
                        onRetry = { viewModel.loadUpcomingGames() }
                    )
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
                        EmptyGamesState(
                            title = "No upcoming games found",
                            message = "Check back later for upcoming game releases"
                        )
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
                    MonthHeader(text = month)
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
