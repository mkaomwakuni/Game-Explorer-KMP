package org.sea.rawg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.Genre
import org.sea.rawg.domain.models.PagedResponse
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.utils.Result

class GenresViewModel(
    private val repository: RawgRepository
) : ViewModel() {

    // State for genres list
    private val _genresState = MutableStateFlow<GenresState>(GenresState.Loading)
    val genresState: StateFlow<GenresState> = _genresState.asStateFlow()

    // State for games by genre
    private val _gamesByGenreState = MutableStateFlow<GamesByGenreState>(GamesByGenreState.Initial)
    val gamesByGenreState: StateFlow<GamesByGenreState> = _gamesByGenreState.asStateFlow()

    // Current pagination state for games by genre
    private var currentGenreId: Int? = null
    private var currentPage = 1
    private var hasMoreGames = true

    // Initialize by loading genres
    init {
        loadGenres()
    }

    fun loadGenres() {
        viewModelScope.launch {
            _genresState.value = GenresState.Loading

            when (val result = repository.getGenres(page = 1, pageSize = 40)) {
                is Result.Success<PagedResponse<Genre>> -> {
                    _genresState.value = GenresState.Success(result.data)
                }

                is Result.Error -> {
                    _genresState.value =
                        GenresState.Error(result.message)
                }
                else -> {
                    // This shouldn't happen with the current Result implementation
                    _genresState.value = GenresState.Error("Unknown error occurred")
                }
            }
        }
    }

    fun loadGamesByGenre(genreId: Int, resetList: Boolean = false) {
        if (resetList) {
            currentPage = 1
            hasMoreGames = true
            currentGenreId = genreId
            _gamesByGenreState.value = GamesByGenreState.Loading
        } else if (!hasMoreGames || _gamesByGenreState.value is GamesByGenreState.Loading) {
            // Don't load more if we're at the end or already loading
            return
        }

        viewModelScope.launch {
            val state = _gamesByGenreState.value
            val existingGames =
                if (state is GamesByGenreState.Success && !resetList) state.data.results else emptyList()

            if (!resetList) {
                _gamesByGenreState.value = GamesByGenreState.LoadingMore(
                    PagedResponse(
                        count = existingGames.size,
                        next = null,
                        previous = null,
                        results = existingGames
                    )
                )
            }

            when (val result = repository.getGamesByGenreId(
                genreId = genreId,
                page = currentPage,
                pageSize = 20
            )) {
                is Result.Success<PagedResponse<Game>> -> {
                    val newData = result.data
                    val combinedGames = existingGames + newData.results

                    hasMoreGames = newData.next != null
                    if (hasMoreGames) currentPage++

                    _gamesByGenreState.value = GamesByGenreState.Success(
                        PagedResponse(
                            count = newData.count,
                            next = newData.next,
                            previous = newData.previous,
                            results = combinedGames
                        )
                    )
                }

                is Result.Error -> {
                    _gamesByGenreState.value =
                        GamesByGenreState.Error(result.message)
                }
                else -> {
                    // This shouldn't happen with the current Result implementation
                    _gamesByGenreState.value = GamesByGenreState.Error("Unknown error occurred")
                }
            }
        }
    }

    fun retryLoadGenres() {
        loadGenres()
    }

    fun retryLoadGamesByGenre() {
        currentGenreId?.let { loadGamesByGenre(it, true) }
    }

    fun loadMoreGames() {
        currentGenreId?.let { loadGamesByGenre(it, false) }
    }
}

// State classes for UI representation
sealed class GenresState {
    object Loading : GenresState()
    data class Success(val data: PagedResponse<Genre>) : GenresState()
    data class Error(val message: String) : GenresState()
}

sealed class GamesByGenreState {
    object Initial : GamesByGenreState()
    object Loading : GamesByGenreState()
    data class LoadingMore(val data: PagedResponse<Game>) : GamesByGenreState()
    data class Success(val data: PagedResponse<Game>) : GamesByGenreState()
    data class Error(val message: String) : GamesByGenreState()
}