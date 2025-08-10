package org.sea.rawg.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sea.rawg.data.model.DLC
import org.sea.rawg.data.model.RedditPost
import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.Screenshot
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.domain.usecases.GetGameDetailsUseCase
import org.sea.rawg.presentation.models.GameState
import org.sea.rawg.utils.Result

class GameDetailsViewModel(
    private val repository: RawgRepository,
    private val getGameDetailsUseCase: GetGameDetailsUseCase
) : BaseViewModel() {
    
    private val _gameDetails = MutableStateFlow<GameState>(GameState.Loading)
    val gameDetails: StateFlow<GameState> = _gameDetails.asStateFlow()

    
    private val _dlcs = MutableStateFlow<List<DLC>>(emptyList())
    val dlcs: StateFlow<List<DLC>> = _dlcs.asStateFlow()

    
    private val _redditPosts = MutableStateFlow<List<RedditPost>>(emptyList())
    val redditPosts: StateFlow<List<RedditPost>> = _redditPosts.asStateFlow()

    
    private val _screenshots = MutableStateFlow<List<Screenshot>>(emptyList())
    val screenshots: StateFlow<List<Screenshot>> = _screenshots.asStateFlow()

    
    private val _similarGames = MutableStateFlow<List<Game>>(emptyList())
    val similarGames: StateFlow<List<Game>> = _similarGames.asStateFlow()

    
    private val _isDlcsLoading = MutableStateFlow(false)
    val isDlcsLoading: StateFlow<Boolean> = _isDlcsLoading.asStateFlow()

    private val _isRedditLoading = MutableStateFlow(false)
    val isRedditLoading: StateFlow<Boolean> = _isRedditLoading.asStateFlow()

    private val _isScreenshotsLoading = MutableStateFlow(false)
    val isScreenshotsLoading: StateFlow<Boolean> = _isScreenshotsLoading.asStateFlow()

    private val _isSimilarGamesLoading = MutableStateFlow(false)
    val isSimilarGamesLoading: StateFlow<Boolean> = _isSimilarGamesLoading.asStateFlow()

    fun loadGameDetails(gameId: Int) {
        _gameDetails.value = GameState.Loading

        viewModelScope.launch {
            getGameDetailsUseCase(gameId).let { result ->
                when (result) {
                    is Result.Success<Game> -> {
                        _gameDetails.value = GameState.Success(result.data)

                        
                        loadRelatedContent(gameId, result.data.name)
                    }
                    is Result.Error -> {
                        _gameDetails.value = GameState.Error(result.message)
                    }
                    else -> {
                        _gameDetails.value = GameState.Error("Unexpected error occurred")
                    }
                }
            }
        }
    }

    private fun loadRelatedContent(gameId: Int, gameName: String) {
        viewModelScope.launch { loadGameDLCs(gameId) }
        viewModelScope.launch { loadGameRedditPosts(gameName) }
        viewModelScope.launch { loadGameScreenshots(gameId) }
        viewModelScope.launch { loadSimilarGames(gameId) }
    }

    private fun loadGameDLCs(gameId: Int) {
        _isDlcsLoading.value = true

        viewModelScope.launch {
            repository.getGameDLCs(gameId).let { result ->
                _dlcs.value = when (result) {
                    is Result.Success<List<DLC>> -> result.data
                    else -> emptyList()
                }
                _isDlcsLoading.value = false
            }
        }
    }

    private fun loadGameRedditPosts(gameName: String) {
        _isRedditLoading.value = true

        viewModelScope.launch {
            repository.getGameRedditPosts(gameName).let { result ->
                _redditPosts.value = when (result) {
                    is Result.Success<List<RedditPost>> -> result.data
                    else -> emptyList()
                }
                _isRedditLoading.value = false
            }
        }
    }

    private fun loadGameScreenshots(gameId: Int) {
        _isScreenshotsLoading.value = true

        viewModelScope.launch {
            repository.getGameScreenshots(gameId).let { result ->
                _screenshots.value = when (result) {
                    is Result.Success<List<Screenshot>> -> result.data
                    else -> emptyList()
                }
                _isScreenshotsLoading.value = false
            }
        }
    }

    private fun loadSimilarGames(gameId: Int) {
        _isSimilarGamesLoading.value = true

        viewModelScope.launch {
            repository.getSimilarGames(gameId).let { result ->
                _similarGames.value = when (result) {
                    is Result.Success<List<Game>> -> result.data
                    else -> emptyList()
                }
                _isSimilarGamesLoading.value = false
            }
        }
    }
}