package org.sea.rawg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sea.rawg.domain.models.Collection
import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.PagedResponse
import org.sea.rawg.domain.usecases.GetCollectionByIdUseCase
import org.sea.rawg.domain.usecases.GetCollectionsUseCase
import org.sea.rawg.domain.usecases.GetGamesForCollectionUseCase
import org.sea.rawg.presentation.models.DataState
import org.sea.rawg.presentation.models.PagedDataState

class CollectionsViewModel(
    private val getCollectionsUseCase: GetCollectionsUseCase,
    private val getCollectionByIdUseCase: GetCollectionByIdUseCase,
    private val getGamesForCollectionUseCase: GetGamesForCollectionUseCase
) : BaseViewModel() {

    // Collections list UI state
    private val _collectionsListState =
        MutableStateFlow<DataState<List<Collection>>>(DataState.Loading)
    val collectionsListState: StateFlow<DataState<List<Collection>>> =
        _collectionsListState.asStateFlow()

    // Single collection UI state
    private val _collectionState = MutableStateFlow<DataState<Collection>>(DataState.Loading)
    val collectionState: StateFlow<DataState<Collection>> = _collectionState.asStateFlow()

    // Games in collection state
    private val _gamesState =
        MutableStateFlow<PagedDataState<PagedResponse<Game>>>(PagedDataState.Loading)
    val gamesState: StateFlow<PagedDataState<PagedResponse<Game>>> = _gamesState.asStateFlow()

    // Currently displayed collection ID
    private var currentCollectionId: Int? = null

    // Pagination data
    private var currentPage = 1
    private var hasMoreGames = true
    private val pageSize = 20

    init {
        loadCollections()
    }

    /**
     * Load all collections for the collections screen
     */
    fun loadCollections() {
        launchWithErrorHandling(
            block = {
                _collectionsListState.value = DataState.Loading
                getCollectionsUseCase()
            },
            onSuccess = { collections ->
                _collectionsListState.value = DataState.Success(collections)
            },
            onError = { errorState ->
                _collectionsListState.value = DataState.Error(errorState.message ?: "Unknown error")
            }
        )
    }

    /**
     * Load a specific collection by ID for the collection details screen
     */
    fun loadCollection(collectionId: Int) {
        if (currentCollectionId != collectionId) {
            // Reset pagination when switching to a new collection
            currentPage = 1
            hasMoreGames = true
            currentCollectionId = collectionId
        }

        launchWithErrorHandling(
            block = {
                _collectionState.value = DataState.Loading
                getCollectionByIdUseCase(collectionId)
            },
            onSuccess = { collection ->
                if (collection != null) {
                    _collectionState.value = DataState.Success(collection)
                } else {
                    _collectionState.value = DataState.Error("Collection not found")
                }
            },
            onError = { errorState ->
                _collectionState.value = DataState.Error(errorState.message ?: "Unknown error")
            }
        )

        // Also load games for this collection
        loadGamesForCollection(resetList = true)
    }

    /**
     * Load games for the current collection with pagination support
     */
    fun loadGamesForCollection(resetList: Boolean = false) {
        val collectionId = currentCollectionId ?: return

        if (resetList) {
            currentPage = 1
            hasMoreGames = true
            _gamesState.value = PagedDataState.Loading
        } else if (!hasMoreGames || _gamesState.value is PagedDataState.LoadingMore) {
            // Don't load more if we're at the end or already loading more
            return
        }

        // Get existing games if we're loading more
        val existingGames =
            if (_gamesState.value is PagedDataState.Success && !resetList) {
                (_gamesState.value as PagedDataState.Success<PagedResponse<Game>>).data.results
            } else {
                emptyList()
            }

        // If loading more, show the loading more state
        if (!resetList) {
            _gamesState.value = PagedDataState.LoadingMore(
                PagedResponse(
                    count = existingGames.size,
                    next = null,
                    previous = null,
                    results = existingGames
                )
            )
        }

        launchWithErrorHandling(
            block = {
                getGamesForCollectionUseCase(
                    collectionId = collectionId,
                    page = currentPage,
                    pageSize = pageSize
                )
            },
            onSuccess = { gamesResponse ->
                // Update pagination state
                hasMoreGames = gamesResponse.next != null
                if (hasMoreGames) currentPage++

                // Combine existing and new games if loading more
                val combinedResults = if (!resetList) {
                    existingGames + gamesResponse.results
                } else {
                    gamesResponse.results
                }

                // Update the state with the combined results
                _gamesState.value = PagedDataState.Success(
                    PagedResponse(
                        count = gamesResponse.count,
                        next = gamesResponse.next,
                        previous = gamesResponse.previous,
                        results = combinedResults
                    )
                )
            },
            onError = { errorState ->
                _gamesState.value = PagedDataState.Error(errorState.message ?: "Unknown error")
            }
        )
    }

    // Load more games for pagination
    fun loadMoreGames() {
        loadGamesForCollection(resetList = false)
    }

    // Retry loading collections
    fun retryCollections() {
        loadCollections()
    }

    // Retry loading the current collection
    fun retryCollection() {
        currentCollectionId?.let { loadCollection(it) }
    }

    // Retry loading games for the current collection
    fun retryGames() {
        loadGamesForCollection(resetList = true)
    }
}
