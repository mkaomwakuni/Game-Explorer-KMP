package org.sea.rawg.presentation.models

/**
 * Generic state container for UI data
 * This replaces multiple specific state classes with a single reusable pattern
 */
sealed class DataState<out T> {
    object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val message: String) : DataState<Nothing>()
}

/**
 * Specialized state container for paginated data that supports load more functionality
 */
sealed class PagedDataState<out T> {
    object Loading : PagedDataState<Nothing>()
    data class LoadingMore<T>(val data: T) : PagedDataState<T>()
    data class Success<T>(val data: T) : PagedDataState<T>()
    data class Error(val message: String) : PagedDataState<Nothing>()
}