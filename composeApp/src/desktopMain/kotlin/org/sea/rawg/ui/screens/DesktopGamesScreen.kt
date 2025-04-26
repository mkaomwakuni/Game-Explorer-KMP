package org.sea.rawg.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import org.sea.rawg.presentation.models.DataState
import org.sea.rawg.ui.component.ErrorState
import org.sea.rawg.ui.component.GameCard
import org.sea.rawg.ui.component.LoadingIndicator
import org.sea.rawg.ui.component.SectionHeader
import org.sea.rawg.ui.component.cards.CollectionCard
import org.sea.rawg.ui.component.cards.GenreCard
import org.sea.rawg.ui.component.cards.PublisherCard
import org.sea.rawg.ui.viewmodel.CollectionsViewModel
import org.sea.rawg.ui.viewmodel.GamesState
import org.sea.rawg.ui.viewmodel.GenresState
import org.sea.rawg.ui.viewmodel.GenresViewModel
import org.sea.rawg.ui.viewmodel.HomeViewModel
import org.sea.rawg.ui.viewmodel.PublishersViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DesktopGamesScreen(navigator: Navigator) {
    val homeViewModel = koinInject<HomeViewModel>()
    val genresViewModel = koinInject<GenresViewModel>()
    val publishersViewModel = koinInject<PublishersViewModel>()
    val collectionsViewModel = koinInject<CollectionsViewModel>()

    val tabItems = listOf("Games", "Genres", "Publishers", "Collections")
    val pagerState = rememberPagerState(initialPage = 0) { tabItems.size }
    val coroutineScope = rememberCoroutineScope()
    var searchExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val selectedTabIndex = pagerState.currentPage

    LaunchedEffect(selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> homeViewModel.initialize()
            1 -> genresViewModel.loadGenres()
            2 -> publishersViewModel.loadPublishers()
            3 -> collectionsViewModel.loadCollections()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val canGoBack by navigator.canGoBack.collectAsState(false)
                    if (canGoBack) {
                        IconButton(
                            onClick = { navigator.goBack() },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Text(
                        "KMP Game Explorer",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    edgePadding = 16.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 24.dp),
                    divider = {},
                    indicator = {},
                    tabs = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                tabItems.forEachIndexed { index, title ->
                                    Tab(
                                        selected = selectedTabIndex == index,
                                        onClick = {
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(index)
                                            }
                                        },
                                        text = {
                                            Text(
                                                text = title,
                                                fontSize = 14.sp,
                                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                            )
                                        },
                                        selectedContentColor = MaterialTheme.colorScheme.primary,
                                        unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.7f
                                        )
                                    )
                                }
                            }
                        }
                    }
                )

                IconButton(onClick = { searchExpanded = !searchExpanded }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = searchExpanded,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                placeholder = { Text("Search games...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                        }) {
                            Text("×", fontSize = 20.sp)
                        }
                    }
                }
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            when (page) {
                0 -> GamesTab(homeViewModel, navigator)
                1 -> GenresTab(genresViewModel, navigator)
                2 -> PublishersTab(publishersViewModel, navigator)
                3 -> CollectionsTab(navigator, collectionsViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesTab(homeViewModel: HomeViewModel, navigator: Navigator) {
    LaunchedEffect(Unit) {
        homeViewModel.resetStates()
        homeViewModel.initialize()
    }

    val popularGames = homeViewModel.popularGamesState.collectAsState().value
    val topRatedGames = homeViewModel.topRatedGamesState.collectAsState().value
    val recentReleases = homeViewModel.newReleasesState.collectAsState().value
    val upcomingGames = homeViewModel.upcomingGamesState.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                GameSection(
                    title = "Popular Games",
                    subtitle = "Trending right now",
                    gameState = popularGames,
                    onRetry = { homeViewModel.fetchPopularGames() },
                    onGameClick = { gameId -> navigator.navigate("game/$gameId") }
                )
            }

            item {
                GameSection(
                    title = "Top Rated Games",
                    subtitle = "The best of the best",
                    gameState = topRatedGames,
                    onRetry = { homeViewModel.fetchTopRatedGames() },
                    onGameClick = { gameId -> navigator.navigate("game/$gameId") }
                )
            }

            item {
                GameSection(
                    title = "Recent Releases",
                    subtitle = "Fresh off the press",
                    gameState = recentReleases,
                    onRetry = { homeViewModel.fetchRecentReleases() },
                    onGameClick = { gameId -> navigator.navigate("game/$gameId") }
                )
            }

            item {
                GameSection(
                    title = "Upcoming Games",
                    subtitle = "Coming soon",
                    gameState = upcomingGames,
                    onRetry = { homeViewModel.fetchUpcomingGames() },
                    onGameClick = { gameId -> navigator.navigate("game/$gameId") }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Sort",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Button(
            onClick = {
                homeViewModel.resetStates()
                homeViewModel.initialize()
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text("Reload Data")
        }
    }
}

@Composable
private fun GameSection(
    title: String,
    subtitle: String,
    gameState: org.sea.rawg.ui.viewmodel.GamesState,
    onRetry: () -> Unit,
    onGameClick: (Int) -> Unit
) {
    SectionHeader(
        title = title,
        subtitle = subtitle,
        actionText = "View All"
    )

    Spacer(modifier = Modifier.height(8.dp))

    when (gameState) {
        is GamesState.Loading -> {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                LoadingIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        is GamesState.Success -> {
            if (gameState.data.results.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(gameState.data.results) { game ->
                        GameCard(
                            game = game,
                            onClick = { onGameClick(game.id) },
                            modifier = Modifier.width(280.dp)
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    Text(
                        text = "No games available",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        is GamesState.Error -> {
            ErrorState(
                message = gameState.message,
                onRetry = onRetry,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
        }
    }
}

@Composable
fun GenresTab(genresViewModel: GenresViewModel, navigator: Navigator) {
    val genresState by genresViewModel.genresState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = genresState) {
            is GenresState.Loading -> {
                LoadingIndicator(modifier = Modifier.fillMaxSize())
            }
            is GenresState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 300.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.data.results) { genre ->
                        GenreCard(
                            genre = genre,
                            onClick = { navigator.navigate("genre/${genre.id}") }
                        )
                    }
                }
            }
            is GenresState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = { genresViewModel.loadGenres() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun PublishersTab(publishersViewModel: PublishersViewModel, navigator: Navigator) {
    val publishersState by publishersViewModel.publishersState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = publishersState) {
            is PublishersViewModel.PublishersState.Loading -> {
                LoadingIndicator(modifier = Modifier.fillMaxSize())
            }
            is PublishersViewModel.PublishersState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 300.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.publishers) { publisher ->
                        PublisherCard(
                            publisher = publisher,
                            onClick = { navigator.navigate("publisher/${publisher.id}") }
                        )
                    }
                }
            }
            is PublishersViewModel.PublishersState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = { publishersViewModel.loadPublishers() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun CollectionsTab(navigator: Navigator, collectionsViewModel: CollectionsViewModel) {
    val collectionsState by collectionsViewModel.collectionsListState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = collectionsState) {
            is DataState.Loading -> {
                LoadingIndicator(modifier = Modifier.fillMaxSize())
            }
            is DataState.Success -> {
                if (state.data.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Collections Yet",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Create a collection to organize your favorite games",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { }
                        ) {
                            Text("Create Collection")
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 300.dp),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.data) { collection ->
                            CollectionCard(
                                collection = collection,
                                onClick = { navigator.navigate("collection/${collection.id}") }
                            )
                        }
                    }
                }
            }
            is DataState.Error -> {
                ErrorState(
                    message = state.message ?: "Failed to load collections",
                    onRetry = { collectionsViewModel.loadCollections() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
