package org.sea.rawg.presentation.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.domain.usecases.GetGameDetailsUseCase
import org.sea.rawg.presentation.models.DataState
import org.sea.rawg.utils.Result
import org.sea.rawg.ui.viewmodel.BaseViewModel

class GameDetailsViewModel(
    private val repository: RawgRepository,
    private val getGameDetailsUseCase: GetGameDetailsUseCase
) : BaseViewModel() {

    private val _gameDetails = MutableStateFlow<DataState<Game>>(DataState.Loading)
    val gameDetails: StateFlow<DataState<Game>> = _gameDetails.asStateFlow()

    fun loadGameDetails(gameId: Int) {
        _gameDetails.value = DataState.Loading

        viewModelScope.launch {
            try {
                when (val result = getGameDetailsUseCase(gameId)) {
                    is Result.Success -> {
                        _gameDetails.value = DataState.Success(result.data)
                    }

                    is Result.Error -> {
                        _gameDetails.value = DataState.Error(result.message)
                    }

                    else -> {
                    }
                }
            } catch (e: Exception) {
                _gameDetails.value = DataState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}