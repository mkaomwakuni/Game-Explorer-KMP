package org.sea.rawg.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.viewmodel.koinViewModel
import org.sea.rawg.domain.models.Publisher
import org.sea.rawg.navigation.NavigationRoutes
import org.sea.rawg.ui.component.AsyncImage
import org.sea.rawg.ui.component.EmptyPublishersState
import org.sea.rawg.ui.component.FullScreenError
import org.sea.rawg.ui.component.LoadingState
import org.sea.rawg.ui.component.cards.PublisherCard
import org.sea.rawg.ui.viewmodel.PublishersViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishersScreen(
    navigator: Navigator = Navigator(),
    viewModel: PublishersViewModel = koinViewModel()
) {
    val publishersState by viewModel.publishersState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val gridState = rememberLazyStaggeredGridState()
    val visibleState = remember { MutableTransitionState(false) }
    val coroutineScope = rememberCoroutineScope()

    val searchQuery by viewModel.searchQuery.collectAsState()
    var showSearch by remember { mutableStateOf(false) }

    visibleState.targetState = true

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    if (showSearch) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Search publishers") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    } else {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                "Publishers",
                                style = MaterialTheme.typography.headlineMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                "Game publishers and studios",
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                actions = {
                    if (showSearch && searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                    IconButton(onClick = { showSearch = !showSearch }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.height(100.dp)
            )
        },
        floatingActionButton = {
            val showScrollToTopButton by remember {
                derivedStateOf { gridState.firstVisibleItemIndex > 0 }
            }

            AnimatedVisibility(
                visible = showScrollToTopButton,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            gridState.animateScrollToItem(0)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(Icons.Default.KeyboardArrowUp, "Scroll to top")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = publishersState) {
                is PublishersViewModel.PublishersState.Loading -> {
                    LoadingState(
                        message = "Loading publishers...",
                        onRetry = { viewModel.loadPublishers(resetList = true) }
                    )
                }

                is PublishersViewModel.PublishersState.Success -> {
                    if (state.publishers.isEmpty()) {
                        EmptyPublishersState()
                    } else {
                        PublishersContent(
                            visibleState = visibleState,
                            publishers = state.publishers,
                            gridState = gridState,
                            padding = padding,
                            navigator = navigator,
                            viewModel = viewModel
                        )
                    }
                }

                is PublishersViewModel.PublishersState.Error -> {
                    FullScreenError(
                        message = state.message,
                        onRetry = { viewModel.loadPublishers(resetList = true) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PublishersContent(
    visibleState: MutableTransitionState<Boolean>,
    publishers: List<Publisher>,
    gridState: androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState,
    padding: PaddingValues,
    navigator: Navigator,
    viewModel: PublishersViewModel
) {
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val hasMore = viewModel.hasMoreItems()

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding() + 80.dp // Extra space for loading indicator
                ),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                state = gridState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = publishers,
                    key = { it.id.toString() }
                ) { publisher ->
                    PublisherCard(
                        publisher = publisher,
                        onClick = {
                            navigator.navigate(
                                NavigationRoutes.publisherDetailsRoute(publisher.id)
                            )
                        }
                    )
                }
            }

            // Optimized pagination logic
            val shouldLoadMore by remember {
                derivedStateOf {
                    val layoutInfo = gridState.layoutInfo
                    val visibleItems = layoutInfo.visibleItemsInfo
                    val totalItems = layoutInfo.totalItemsCount

                    if (visibleItems.isEmpty() || totalItems == 0 || !hasMore || isLoadingMore) {
                        false
                    } else {
                        val lastVisibleIndex = visibleItems.maxOfOrNull { it.index } ?: 0
                        val threshold = 10 // Load when 8 items from bottom
                        lastVisibleIndex >= totalItems - threshold
                    }
                }
            }

            // Single LaunchedEffect for pagination with proper debouncing
            LaunchedEffect(shouldLoadMore) {
                if (shouldLoadMore) {
                    delay(400) // Debounce rapid scroll events
                    if (shouldLoadMore && !isLoadingMore && hasMore) {
                        viewModel.loadPublishers()
                    }
                }
            }

            // Loading indicator overlay at bottom
            if (isLoadingMore) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Loading more...",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            } else if (!hasMore && publishers.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No more publishers to load",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun PublisherCard(
    publisher: Publisher,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val height = remember { 180.dp + (Random.nextInt(5) * 20).dp }
    val cornerRadius = remember { 8.dp + (Random.nextInt(3) * 4).dp }
    val topPadding = remember { (Random.nextInt(5) * 2).dp }
    val horizontalPadding = remember { (Random.nextInt(3) * 2).dp }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .padding(top = topPadding, start = horizontalPadding, end = horizontalPadding)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            publisher.image_background?.let { imageUrl ->
                AsyncImage(
                    url = imageUrl,
                    contentDescription = "${publisher.name} background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(cornerRadius))
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.5f),
                                            Color.Black.copy(alpha = 0.8f)
                                        ),
                                        startY = size.height * 0.4f
                                    ),
                                    blendMode = BlendMode.SrcAtop
                                )
                            }
                        }
                )
            }

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = publisher.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${publisher.games_count} games",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "Publisher",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White
                    )
                }
            }
        }
    }
}