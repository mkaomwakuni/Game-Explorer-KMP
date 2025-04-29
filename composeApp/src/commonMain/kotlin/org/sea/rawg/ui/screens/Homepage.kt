package org.sea.rawg.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import org.sea.rawg.ui.FullScreenLoading
import org.sea.rawg.ui.component.ErrorState
import org.sea.rawg.ui.component.GameCard
import org.sea.rawg.ui.component.SectionHeader
import org.sea.rawg.ui.viewmodel.GamesState
import org.sea.rawg.ui.viewmodel.HomeViewModel
import org.sea.rawg.navigation.NavigationRoutes

/**
 * Homepage screen component that displays various game categories.
 * 
 * This screen displays four categories of games:
 * - Popular Games
 * - Upcoming Games
 * - Recent Releases
 * - Top Rated Games
 * 
 * @param navigator Navigation controller for screen transitions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homepage(navigator: Navigator) {
    // Get ViewModel instance using Koin
    val viewModel = koinViewModel<HomeViewModel>()
    
    // Collect all game category states from the ViewModel
    val popularGamesState = viewModel.popularGamesState.collectAsState().value
    val upcomingGamesState = viewModel.upcomingGamesState.collectAsState().value
    val recentReleasesState = viewModel.newReleasesState.collectAsState().value
    val topRatedGamesState = viewModel.topRatedGamesState.collectAsState().value

    // Initialize data when the screen first composes
    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // App Bar
        TopAppBar(
            title = {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        "Game Explorer",
                        style = MaterialTheme.typography.headlineMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "Discover amazing games",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    navigator.navigate(NavigationRoutes.SEARCH)
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.height(100.dp)
        )

        // Content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // 1. Popular Games Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    title = "Popular Games",
                    actionText = "View All",
                    onActionClick = { /* Navigate to full list */ },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                GameCategoryRow(
                    gamesState = popularGamesState,
                    onRetry = { viewModel.fetchPopularGames() },
                    onGameClick = { gameId ->
                        navigator.navigate(
                            NavigationRoutes.gameDetailsRoute(
                                gameId
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // 2. Upcoming Games Section
            item {
                SectionHeader(
                    title = "Upcoming Games",
                    actionText = "View All",
                    onActionClick = { navigator.navigate(NavigationRoutes.UPCOMING_GAMES) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                GameCategoryRow(
                    gamesState = upcomingGamesState,
                    onRetry = { viewModel.fetchUpcomingGames() },
                    onGameClick = { gameId ->
                        navigator.navigate(
                            NavigationRoutes.gameDetailsRoute(
                                gameId
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // 3. Recent Releases Section
            item {
                SectionHeader(
                    title = "Recent Releases",
                    actionText = "View All",
                    onActionClick = { /* Navigate to full list */ },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                GameCategoryRow(
                    gamesState = recentReleasesState,
                    onRetry = { viewModel.fetchRecentReleases() },
                    onGameClick = { gameId ->
                        navigator.navigate(
                            NavigationRoutes.gameDetailsRoute(
                                gameId
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // 4. Top Rated Games Section
            item {
                SectionHeader(
                    title = "Top Rated Games",
                    actionText = "View All",
                    onActionClick = { /* Navigate to full list */ },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                GameCategoryRow(
                    gamesState = topRatedGamesState,
                    onRetry = { viewModel.fetchTopRatedGames() },
                    onGameClick = { gameId ->
                        navigator.navigate(
                            NavigationRoutes.gameDetailsRoute(
                                gameId
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Displays a horizontal row of games based on the current state
 * 
 * @param gamesState The current state of the game data (loading, success, error)
 * @param onRetry Callback to retry loading if there was an error
 * @param onGameClick Callback when a game is clicked, with the game ID
 * @param modifier Optional modifier for the layout
 */
@Composable
private fun GameCategoryRow(
    gamesState: GamesState,
    onRetry: () -> Unit,
    onGameClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (gamesState) {
        is GamesState.Loading -> {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                FullScreenLoading()
            }
        }

        is GamesState.Error -> {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                ErrorState(
                    message = gamesState.message,
                    onRetry = onRetry
                )
            }
        }

        is GamesState.Success -> {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(gamesState.data.results) { game ->
                    GameCard(
                        game = game,
                        onClick = { onGameClick(game.id) },
                        modifier = Modifier.width(260.dp)
                    )
                }
            }
        }
    }
}