package org.sea.rawg.utils

import org.sea.rawg.data.model.ErrorType

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    
    data class Error(
        val message: String,
        val type: ErrorType = ErrorType.GENERIC
    ) : Result<Nothing>()
    
    object Loading : Result<Nothing>()
    
    val isSuccess: Boolean
        get() = this is Success
    
    val isError: Boolean
        get() = this is Error
    
    val isLoading: Boolean
        get() = this is Loading
    
    fun getDataOrNull(): T? = if (this is Success) data else null
    
    inline fun <R> map(transform: (T) -> R): Result<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> this
            is Loading -> Loading
        }
    }
    
    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }
    
    inline fun onError(action: (String, ErrorType) -> Unit): Result<T> {
        if (this is Error) action(message, type)
        return this
    }
    
    inline fun onLoading(action: () -> Unit): Result<T> {
        if (this is Loading) action()
        return this
    }
    
    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        
        fun error(message: String, type: ErrorType = ErrorType.GENERIC): Result<Nothing> =
            Error(message, type)
        
        fun <T> loading(): Result<T> = Loading
    }
}