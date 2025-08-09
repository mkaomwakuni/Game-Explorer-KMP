package org.sea.rawg.presentation.models

sealed class DataState<out T> {
    object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val message: String) : DataState<Nothing>()
}

sealed class PagedDataState<out T> {
    object Loading : PagedDataState<Nothing>()
    data class LoadingMore<T>(val data: T) : PagedDataState<T>()
    data class Success<T>(val data: T) : PagedDataState<T>()
    data class Error(val message: String) : PagedDataState<Nothing>()
}