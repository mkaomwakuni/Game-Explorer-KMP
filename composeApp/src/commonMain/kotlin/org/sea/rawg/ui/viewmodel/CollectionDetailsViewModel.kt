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
import org.sea.rawg.domain.usecases.GetGamesForCollectionUseCase

class CollectionDetailsViewModel(
    private val collectionId: Int,
    private val getCollectionByIdUseCase: GetCollectionByIdUseCase,
    private val getGamesForCollectionUseCase: GetGamesForCollectionUseCase
) : ViewModel() {

    
    private val _collectionState =
        MutableStateFlow<CollectionDetailState>(CollectionDetailState.Loading)
    val collectionState: StateFlow<CollectionDetailState> = _collectionState.asStateFlow()

    
    private val _gamesState =
        MutableStateFlow<GamesInCollectionState>(GamesInCollectionState.Loading)
    val gamesState: StateFlow<GamesInCollectionState> = _gamesState.asStateFlow()

    
    private var currentPage = 1
    private var hasMoreGames = true
    private val pageSize = 20

    init {
        loadCollection()
        loadGamesForCollection(resetList = true)
    }

    private fun loadCollection() {
        viewModelScope.launch {
            try {
                _collectionState.value = CollectionDetailState.Loading
                val collection = getCollectionByIdUseCase(collectionId)
                if (collection != null) {
                    _collectionState.value = CollectionDetailState.Success(collection)
                } else {
                    _collectionState.value = CollectionDetailState.Error("Collection not found")
                }
            } catch (e: Exception) {
                _collectionState.value = CollectionDetailState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadGamesForCollection(resetList: Boolean = false) {
        if (resetList) {
            currentPage = 1
            hasMoreGames = true
            _gamesState.value = GamesInCollectionState.Loading
        } else if (!hasMoreGames || _gamesState.value is GamesInCollectionState.LoadingMore) {
            
            return
        }

        viewModelScope.launch {
            try {
                
                val existingGames =
                    if (_gamesState.value is GamesInCollectionState.Success && !resetList) {
                        (_gamesState.value as GamesInCollectionState.Success).games.results
                    } else {
                        emptyList()
                    }

                
                if (!resetList) {
                    _gamesState.value = GamesInCollectionState.LoadingMore(
                        PagedResponse(
                            count = existingGames.size,
                            next = null,
                            previous = null,
                            results = existingGames
                        )
                    )
                }

                
                val gamesResponse = getGamesForCollectionUseCase(
                    collectionId = collectionId,
                    page = currentPage,
                    pageSize = pageSize
                )

                
                hasMoreGames = gamesResponse.next != null
                if (hasMoreGames) currentPage++

                
                val combinedResults = if (!resetList) {
                    existingGames + gamesResponse.results
                } else {
                    gamesResponse.results
                }

                
                _gamesState.value = GamesInCollectionState.Success(
                    PagedResponse(
                        count = gamesResponse.count,
                        next = gamesResponse.next,
                        previous = gamesResponse.previous,
                        results = combinedResults
                    )
                )
            } catch (e: Exception) {
                _gamesState.value = GamesInCollectionState.Error(e.message ?: "Unknown error")
            }
        }
    }

    
    fun loadMoreGames() {
        loadGamesForCollection(resetList = false)
    }

    
    fun retryLoadCollection() {
        loadCollection()
    }

    
    fun retryLoadGames() {
        loadGamesForCollection(resetList = true)
    }
}

sealed class CollectionDetailState {
    object Loading : CollectionDetailState()
    data class Success(val collection: Collection) : CollectionDetailState()
    data class Error(val message: String) : CollectionDetailState()
}

sealed class GamesInCollectionState {
    object Loading : GamesInCollectionState()
    data class LoadingMore(val games: PagedResponse<Game>) : GamesInCollectionState()
    data class Success(val games: PagedResponse<Game>) : GamesInCollectionState()
    data class Error(val message: String) : GamesInCollectionState()
}