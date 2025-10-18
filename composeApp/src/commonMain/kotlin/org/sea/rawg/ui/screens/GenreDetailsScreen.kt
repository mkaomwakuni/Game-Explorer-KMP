package org.sea.rawg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import org.koin.compose.viewmodel.koinViewModel
import org.sea.rawg.domain.models.Game
import org.sea.rawg.navigation.NavigationRoutes
import org.sea.rawg.ui.component.AsyncImage
import org.sea.rawg.ui.component.LoadingIndicator
import org.sea.rawg.ui.viewmodel.GamesByGenreState
import org.sea.rawg.ui.viewmodel.GenresState
import org.sea.rawg.ui.viewmodel.GenresViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreDetailsScreen(
    navigator: Navigator,
    genreId: Int,
    viewModel: GenresViewModel = koinViewModel()
) {
    val gamesByGenreState by viewModel.gamesByGenreState.collectAsState()
    val genresState by viewModel.genresState.collectAsState()
    val gridState = rememberLazyGridState()

    // Selected genre (if available from the genres list)
    val selectedGenre = genresState.let { state ->
        if (state is GenresState.Success) {
            state.data.results.find { it.id == genreId }
        } else null
    }

    // Load games when screen appears
    // Only reload games when the genre changes, not every recomposition
    LaunchedEffect(genreId) {
        viewModel.loadGamesByGenre(genreId, true)
    }

    // Handle scrolling to load more games
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null) {
                    val totalItems =
                        (gamesByGenreState as? GamesByGenreState.Success)?.data?.results?.size ?: 0
                    // If we're close to the end of the list, load more games
                    if (lastVisibleIndex >= totalItems - 5 && totalItems > 0) {
                        viewModel.loadMoreGames()
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            selectedGenre?.name ?: "Genre Games",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        if (selectedGenre != null) {
                            Text(
                                "${selectedGenre.games_count} games",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.goBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = gamesByGenreState) {
                is GamesByGenreState.Initial, is GamesByGenreState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            LoadingIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading games...",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                is GamesByGenreState.Success -> {
                    val games = state.data.results
                    if (games.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "No games found for this genre",
                                    style = MaterialTheme.typography.headlineMedium,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Try another genre or check back later",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 160.dp),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            state = gridState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = games,
                                key = { it.id }
                            ) { game ->
                                GameCard(
                                    game = game,
                                    onClick = {
                                        navigator.navigate(
                                            NavigationRoutes.gameDetailsRoute(game.id)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                is GamesByGenreState.LoadingMore -> {
                    // Show both the list and a loading indicator at the bottom
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 160.dp),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            state = gridState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = state.data.results,
                                key = { it.id }
                            ) { game ->
                                GameCard(
                                    game = game,
                                    onClick = {
                                        navigator.navigate(
                                            NavigationRoutes.gameDetailsRoute(game.id)
                                        )
                                    }
                                )
                            }

                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }

                is GamesByGenreState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Unable to load games",
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(onClick = { viewModel.retryLoadGamesByGenre() }) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Try Again")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(220.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.small, // 2dp rounded corners
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp // No shadow for better blending
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // Fully transparent container
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Game image
            game.background_image?.let { imageUrl ->
                AsyncImage(
                    url = imageUrl,
                    contentDescription = "${game.name} image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Gradient overlay for fading effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background.copy(alpha = 0.6f), // Fade from background color at top
                                Color.Transparent, // Transparent in middle
                                Color.Black.copy(alpha = 0.6f) // Dark at bottom
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            // Game info at the bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Rating if available
                game.rating?.let { rating ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${rating}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )

                        LinearProgressIndicator(
                            progress = { rating.toFloat() / 5f },
                            modifier = Modifier
                                .height(4.dp)
                                .width(40.dp),
                            color = when {
                                rating >= 4.0 -> MaterialTheme.colorScheme.primary
                                rating >= 3.0 -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.error
                            }
                        )
                    }
                }
            }
        }
    }
}