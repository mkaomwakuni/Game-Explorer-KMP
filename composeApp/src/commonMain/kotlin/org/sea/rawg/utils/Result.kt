package org.sea.rawg.utils

import org.sea.rawg.data.model.ErrorType

/**
 * A sealed class representing the result of an operation.
 * This is used throughout the application to standardize error handling
 * and success responses.
 *
 * @param T The type of data in case of success
 */
sealed class Result<out T> {
    /**
     * Represents a successful operation with data
     *
     * @param data The data returned by the operation
     */
    data class Success<T>(val data: T) : Result<T>()
    
    /**
     * Represents a failed operation with an error message and type
     *
     * @param message Error message describing what went wrong
     * @param type The type of error that occurred
     */
    data class Error(
        val message: String,
        val type: ErrorType = ErrorType.GENERIC
    ) : Result<Nothing>()
    
    /**
     * Represents an operation in progress
     */
    object Loading : Result<Nothing>()
    
    /**
     * Checks if the result is a success
     */
    val isSuccess: Boolean
        get() = this is Success
    
    /**
     * Checks if the result is an error
     */
    val isError: Boolean
        get() = this is Error
    
    /**
     * Checks if the result is loading
     */
    val isLoading: Boolean
        get() = this is Loading
    
    /**
     * Gets the data if the result is a success, or null otherwise
     */
    fun getDataOrNull(): T? = if (this is Success) data else null
    
    /**
     * Transforms the result using the given transform function if it's a success
     *
     * @param transform Function to transform the data
     * @return A new Result with the transformed data, or the original error
     */
    inline fun <R> map(transform: (T) -> R): Result<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> this
            is Loading -> Loading
        }
    }
    
    /**
     * Executes the given action if the result is a success
     *
     * @param action The action to execute with the data
     * @return This result
     */
    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }
    
    /**
     * Executes the given action if the result is an error
     *
     * @param action The action to execute with the error message and type
     * @return This result
     */
    inline fun onError(action: (String, ErrorType) -> Unit): Result<T> {
        if (this is Error) action(message, type)
        return this
    }
    
    /**
     * Executes the given action if the result is loading
     *
     * @param action The action to execute
     * @return This result
     */
    inline fun onLoading(action: () -> Unit): Result<T> {
        if (this is Loading) action()
        return this
    }
    
    companion object {
        /**
         * Creates a Success result with the given data
         */
        fun <T> success(data: T): Result<T> = Success(data)
        
        /**
         * Creates an Error result with the given message and type
         */
        fun error(message: String, type: ErrorType = ErrorType.GENERIC): Result<Nothing> =
            Error(message, type)
        
        /**
         * Creates a Loading result
         */
        fun <T> loading(): Result<T> = Loading
    }
}