package org.sea.rawg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.viewmodel.koinViewModel
import org.sea.rawg.domain.models.Genre
import org.sea.rawg.navigation.NavigationRoutes
import org.sea.rawg.ui.component.AsyncImage
import org.sea.rawg.ui.component.EmptyState
import org.sea.rawg.ui.component.FullScreenError
import org.sea.rawg.ui.component.LoadingState
import org.sea.rawg.ui.component.cards.GenreCard
import org.sea.rawg.ui.viewmodel.GenresState
import org.sea.rawg.ui.viewmodel.GenresViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenresScreen(
    navigator: Navigator = Navigator(),
    viewModel: GenresViewModel = koinViewModel()
) {
    val genresState by viewModel.genresState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        "Genres",
                        style = MaterialTheme.typography.headlineMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "Game categories and genres",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    // Navigate to search when implemented
                    navigator.navigate("/search")
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

        // Content based on state
        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = genresState) {
                is GenresState.Loading -> {
                    LoadingState(
                        message = "Loading genres...",
                        onRetry = { viewModel.retryLoadGenres() }
                    )
                }

                is GenresState.Success -> {
                    if (state.data.results.isEmpty()) {
                        EmptyState(
                            title = "No genres found",
                            message = "We couldn't find any game genres at this time",
                            icon = Icons.Default.Category
                        )
                    } else {
                        GenresGrid(
                            genres = state.data.results,
                            onGenreClick = { genre ->
                                // Navigate to genre details screen with genre ID
                                navigator.navigate(
                                    NavigationRoutes.genreDetailsRoute(
                                        genre.id
                                    )
                                )
                            }
                        )
                    }
                }

                is GenresState.Error -> {
                    FullScreenError(
                        message = state.message,
                        onRetry = { viewModel.retryLoadGenres() }
                    )
                }
            }
        }
    }
}

@Composable
fun GenresGrid(
    genres: List<Genre>,
    onGenreClick: (Genre) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(items = genres) { genre ->
            GenreCard(
                genre = genre,
                onClick = { onGenreClick(genre) }
            )
        }
    }
}
