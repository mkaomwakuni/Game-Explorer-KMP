package org.sea.rawg.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.sea.rawg.data.model.BaseGameModel
import org.sea.rawg.data.repository.GameRepository
import org.sea.rawg.data.repository.GamesState

class HomeViewModel(
    private val repository: GameRepository = GameRepository()
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    // State for released games
    val releasedGames = mutableStateOf<GamesState>(GamesState.Loading)

    // Current page for pagination
    private var currentPage = 1

    init {
        // Load initial data when ViewModel is created
        loadReleasedGames(1)
    }

    fun loadReleasedGames(page: Int = 1) {
        viewModelScope.launch {
            repository.getReleasedGames(page).collectLatest { state ->
                releasedGames.value = state
                if (state is GamesState.Success) {
                    currentPage = page
                }
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