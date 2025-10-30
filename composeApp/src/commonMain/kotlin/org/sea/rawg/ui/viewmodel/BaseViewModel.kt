package org.sea.rawg.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sea.rawg.data.model.ErrorType

abstract class BaseViewModel : ViewModel() {

    private val viewModelJob = SupervisorJob()
    protected val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<ErrorState?>(null)
    val error: StateFlow<ErrorState?> = _error.asStateFlow()

    data class ErrorState(
        val message: String,
        val type: ErrorType = ErrorType.GENERIC
    )

    protected fun <T> launchWithErrorHandling(
        block: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: ((ErrorState) -> Unit)? = null
    ): Job {
        return viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = block()
                onSuccess(result)
                _error.value = null
            } catch (e: Exception) {
                val errorMessage = e.message ?: "An unknown error occurred"
                val errorState = ErrorState(errorMessage)
                _error.value = errorState
                onError?.invoke(errorState)
            } finally {
                _isLoading.value = false
            }
        }
    }

    protected fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    protected fun setError(message: String, type: ErrorType = ErrorType.GENERIC) {
        _error.value = ErrorState(message, type)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}