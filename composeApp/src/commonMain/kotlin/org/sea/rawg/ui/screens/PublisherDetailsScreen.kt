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
import androidx.compose.material.icons.filled.Business
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
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.PagedResponse
import org.sea.rawg.domain.models.Publisher
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.navigation.NavigationRoutes
import org.sea.rawg.ui.component.AsyncImage
import org.sea.rawg.ui.component.LoadingIndicator
import org.sea.rawg.ui.viewmodel.PublishersViewModel
import org.sea.rawg.utils.NetworkResource

sealed class GamesByPublisherState {
    object Loading : GamesByPublisherState()
    data class Success(val games: List<Game>) : GamesByPublisherState()
    data class Error(val message: String? = null) : GamesByPublisherState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublisherDetailsScreen(
    navigator: Navigator,
    publisherId: Int,
    viewModel: PublishersViewModel = koinViewModel()
) {
    val publishersState by viewModel.publishersState.collectAsState()
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    // State for publisher games
    val gamesByPublisherState =
        remember { mutableStateOf<GamesByPublisherState>(GamesByPublisherState.Loading) }
    var currentPage = remember { mutableStateOf(1) }
    var isLoadingMoreGames = remember { mutableStateOf(false) }
    var hasMoreGames = remember { mutableStateOf(true) }

    // Repository for direct API access
    val repository = koinInject<RawgRepository>()

    // Find the selected publisher from the publishers list
    val selectedPublisher = remember(publishersState, publisherId) {
        if (publishersState is PublishersViewModel.PublishersState.Success) {
            (publishersState as PublishersViewModel.PublishersState.Success).publishers.find { it.id == publisherId }
        } else null
    }

    // Function to load games by publisher
    fun loadGamesByPublisher(refresh: Boolean = false) {
        if (refresh) {
            currentPage.value = 1
            hasMoreGames.value = true
            gamesByPublisherState.value = GamesByPublisherState.Loading
        } else if (!hasMoreGames.value || isLoadingMoreGames.value) {
            return
        }

        coroutineScope.launch {
            try {
                isLoadingMoreGames.value = true

                val result = repository.getGames(
                    page = currentPage.value,
                    pageSize = 20,
                    ordering = "-added",
                    additionalParams = mapOf("publishers" to publisherId.toString())
                )

                when (result) {
                    is NetworkResource.Success -> {
                        val newData = result.data
                        hasMoreGames.value = newData.next != null

                        if (currentPage.value == 1 || refresh) {
                            gamesByPublisherState.value =
                                GamesByPublisherState.Success(newData.results)
                        } else {
                            // Append to existing games
                            val currentGames =
                                (gamesByPublisherState.value as? GamesByPublisherState.Success)?.games
                                    ?: emptyList()
                            val combinedGames = currentGames + newData.results
                            gamesByPublisherState.value =
                                GamesByPublisherState.Success(combinedGames)
                        }

                        // Increment page for next load
                        if (hasMoreGames.value) {
                            currentPage.value++
                        }
                    }

                    is NetworkResource.Error -> {
                        gamesByPublisherState.value = GamesByPublisherState.Error(
                            result.message ?: "Failed to load games"
                        )
                    }

                    else -> {
                        // Handle other states if needed
                    }
                }
            } catch (e: Exception) {
                gamesByPublisherState.value = GamesByPublisherState.Error(e.message)
            } finally {
                isLoadingMoreGames.value = false
            }
        }
    }

    // Load games when screen appears
    LaunchedEffect(publisherId) {
        loadGamesByPublisher(refresh = true)
    }

    // Handle scroll to load more
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null) {
                    val gamesList =
                        (gamesByPublisherState.value as? GamesByPublisherState.Success)?.games
                            ?: emptyList()
                    // If we're close to the end of the list, load more games
                    if (lastVisibleIndex >= gamesList.size - 5 && gamesList.isNotEmpty() && hasMoreGames.value && !isLoadingMoreGames.value) {
                        loadGamesByPublisher(refresh = false)
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            // Removed TopAppBar here
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Publisher header
            selectedPublisher?.let { publisher ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Publisher header with background image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        // Background image
                        publisher.image_background?.let { imageUrl ->
                            AsyncImage(
                                url = imageUrl,
                                contentDescription = "${publisher.name} background",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        
                        // Gradient overlay for better text readability
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.6f),
                                            Color.Black.copy(alpha = 0.3f)
                                        )
                                    )
                                )
                        )
                        
                        // Top app bar positioned within the header image
                        TopAppBar(
                            title = { 
                                Column {
                                    Text(
                                        selectedPublisher?.name ?: "Publisher Games",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (selectedPublisher != null) {
                                        Text(
                                            "${selectedPublisher.games_count} games",
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
                                        contentDescription = "Go back",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent,
                                titleContentColor = Color.White
                            )
                        )

                        // Overlay with publisher info
                        Surface(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Business,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        "Publisher",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    publisher.name,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    "${publisher.games_count} games published",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }

                    // Games by this publisher section
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Games by ${publisher.name}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    when (val state = gamesByPublisherState.value) {
                        is GamesByPublisherState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator()
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Loading games...",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }

                        is GamesByPublisherState.Success -> {
                            val games = state.games
                            if (games.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            text = "No games found for this publisher",
                                            style = MaterialTheme.typography.titleMedium,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "The games for this publisher are currently not available.",
                                            style = MaterialTheme.typography.bodyMedium,
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
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(
                                        items = games,
                                        key = { it.id }
                                    ) { game ->
                                        PublisherGameCard(
                                            game = game,
                                            onClick = {
                                                navigator.navigate(
                                                    NavigationRoutes.gameDetailsRoute(game.id)
                                                )
                                            }
                                        )
                                    }

                                    // Show loading indicator at the bottom while loading more
                                    if (isLoadingMoreGames.value) {
                                        item {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(
                                                        24.dp
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        is GamesByPublisherState.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Error,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Unable to load games",
                                        style = MaterialTheme.typography.titleMedium,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = state.message ?: "Unknown error occurred",
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(onClick = {
                                        loadGamesByPublisher(refresh = true)
                                    }) {
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
            } ?: run {
                // Publisher not found
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (publishersState) {
                        is PublishersViewModel.PublishersState.Loading -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Loading publisher details...",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        else -> {
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
                                    "Publisher not found",
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "We couldn't find the publisher with ID $publisherId",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Button(onClick = {
                                    navigator.goBack()
                                }) {
                                    Text("Go Back")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PublisherGameCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(220.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.small,
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
                            text = "${rating.toFloat()}",
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