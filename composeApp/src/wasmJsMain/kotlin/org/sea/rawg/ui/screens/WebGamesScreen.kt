package org.sea.rawg.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import org.sea.rawg.domain.models.PagedResponse
import org.sea.rawg.ui.component.ErrorState
import org.sea.rawg.ui.component.GameCard
import org.sea.rawg.ui.component.LoadingIndicator
import org.sea.rawg.ui.component.SectionHeader
import org.sea.rawg.ui.component.cards.GenreCard
import org.sea.rawg.ui.component.cards.PublisherCard
import org.sea.rawg.ui.viewmodel.GenresState
import org.sea.rawg.ui.viewmodel.GenresViewModel
import org.sea.rawg.ui.viewmodel.GamesState
import org.sea.rawg.ui.viewmodel.HomeViewModel
import org.sea.rawg.ui.viewmodel.PublishersViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WebGamesScreen(navigator: Navigator) {
    // Get ViewModels from KoinInject
    val homeViewModel = koinInject<HomeViewModel>()
    val genresViewModel = koinInject<GenresViewModel>()
    val publishersViewModel = koinInject<PublishersViewModel>()

    // Tab state
    val tabItems = listOf("Games", "Genres", "Publishers")
    val pagerState = rememberPagerState(initialPage = 0) { tabItems.size }
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    val selectedTabIndex = pagerState.currentPage
    var searchVisible by remember { mutableStateOf(false) }

    // Load data based on current tab
    LaunchedEffect(selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> homeViewModel.refresh()
            1 -> genresViewModel.loadGenres()
            2 -> publishersViewModel.loadPublishers()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button, if available in navigation history
                val canGoBack by navigator.canGoBack.collectAsState(false)
                if (canGoBack) {
                    IconButton(
                        onClick = { navigator.goBack() },
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // App title at left
                Text(
                    "KMP Game Explorer",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    edgePadding = 16.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
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

                // Search icon at right
                IconButton(onClick = { searchVisible = !searchVisible }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Search field
        AnimatedVisibility(
            visible = searchVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search games...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Text("×", fontSize = 20.sp)
                        }
                    }
                }
            )
        }

        // Tab Content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> GamesTab(homeViewModel, navigator)
                1 -> GenresTab(genresViewModel, navigator)
                2 -> PublishersTab(publishersViewModel, navigator)
            }
        }
    }
}

@Composable
fun GamesTab(homeViewModel: HomeViewModel, navigator: Navigator) {
    val popularGamesState by homeViewModel.popularGames.collectAsState()
    val topRatedGamesState by homeViewModel.topRatedGames.collectAsState()
    val recentReleasesState by homeViewModel.recentReleases.collectAsState()
    val upcomingGamesState by homeViewModel.upcomingGames.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Popular Games Section
        item {
            SectionHeader(
                title = homeViewModel.popularGamesTitle,
                subtitle = "Trending right now"
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (val state = popularGamesState) {
                is GamesState.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                }

                is GamesState.Success -> {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(state.data.results) { game ->
                            GameCard(
                                game = game,
                                onClick = { navigator.navigate("game/${game.id}") },
                                modifier = Modifier.width(240.dp)
                            )
                        }
                    }
                }
                is GamesState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { homeViewModel.refresh() },
                        modifier = Modifier.fillMaxWidth().height(150.dp)
                    )
                }
            }
        }

        // Top Rated Games Section
        item {
            SectionHeader(
                title = homeViewModel.topRatedGamesTitle,
                subtitle = "The best of the best"
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (val state = topRatedGamesState) {
                is GamesState.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                }

                is GamesState.Success -> {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(state.data.results) { game ->
                            GameCard(
                                game = game,
                                onClick = { navigator.navigate("game/${game.id}") },
                                modifier = Modifier.width(240.dp)
                            )
                        }
                    }
                }
                is GamesState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { homeViewModel.refresh() },
                        modifier = Modifier.fillMaxWidth().height(150.dp)
                    )
                }
            }
        }

        // Recent Releases Section
        item {
            SectionHeader(
                title = homeViewModel.recentReleasesTitle,
                subtitle = "Fresh off the press"
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (val state = recentReleasesState) {
                is GamesState.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                }

                is GamesState.Success -> {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(state.data.results) { game ->
                            GameCard(
                                game = game,
                                onClick = { navigator.navigate("game/${game.id}") },
                                modifier = Modifier.width(240.dp)
                            )
                        }
                    }
                }
                is GamesState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { homeViewModel.refresh() },
                        modifier = Modifier.fillMaxWidth().height(150.dp)
                    )
                }
            }
        }

        // Upcoming Games Section
        item {
            SectionHeader(
                title = homeViewModel.upcomingGamesTitle,
                subtitle = "Coming soon"
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (val state = upcomingGamesState) {
                is GamesState.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                }

                is GamesState.Success -> {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(state.data.results) { game ->
                            GameCard(
                                game = game,
                                onClick = { navigator.navigate("game/${game.id}") },
                                modifier = Modifier.width(240.dp)
                            )
                        }
                    }
                }
                is GamesState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { homeViewModel.refresh() },
                        modifier = Modifier.fillMaxWidth().height(150.dp)
                    )
                }
            }
        }

        // Footer spacing
        item {
            Spacer(modifier = Modifier.height(60.dp))
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
                    columns = GridCells.Adaptive(minSize = 250.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
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
                    columns = GridCells.Adaptive(minSize = 250.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
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