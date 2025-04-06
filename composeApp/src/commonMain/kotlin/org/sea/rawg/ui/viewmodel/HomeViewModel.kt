package org.sea.rawg.ui.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sea.rawg.data.remote.GamesApiService
import org.sea.rawg.data.remote.GamesApiServiceImpl
import org.sea.rawg.data.repository.GamesState
import org.sea.rawg.data.repository.RawgRepositoryImpl
import org.sea.rawg.utils.NetworkResource

/**
 * ViewModel for Home screen
 * Using StateFlow for reactive UI updates
 */
class HomeViewModel {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Create repository and use case
    private val apiService: GamesApiService = GamesApiServiceImpl()
    private val repository = RawgRepositoryImpl(apiService)

    // UI state
    private val _releasedGames = MutableStateFlow<GamesState>(GamesState.Loading)
    val releasedGames: StateFlow<GamesState> = _releasedGames.asStateFlow()

    // Current page for pagination
    private var currentPage = 1

    init {
        // Load initial data when ViewModel is created
        loadReleasedGames(1)
    }

    fun loadReleasedGames(page: Int = 1) {
        _releasedGames.value = GamesState.Loading

        coroutineScope.launch {
            try {
                val result = repository.getGames(page, 20, "-released")
                when (result) {
                    is NetworkResource.Success -> {
                        println("Released games loaded successfully")
                        _releasedGames.value = GamesState.Success(result.data)
                        currentPage = page
                    }

                    is NetworkResource.Error -> {
                        println("Error loading released games: ${result.message}")
                        _releasedGames.value = GamesState.Error(result.message)
                    }

                    else -> {
                        println("Unexpected state in released games")
                    }
                }
            } catch (e: Exception) {
                println("Exception in loadReleasedGames: ${e.message}")
                _releasedGames.value = GamesState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun loadNextPage() {
        loadReleasedGames(currentPage + 1)
    }

    fun refresh() {
        loadReleasedGames(1)
    }
}