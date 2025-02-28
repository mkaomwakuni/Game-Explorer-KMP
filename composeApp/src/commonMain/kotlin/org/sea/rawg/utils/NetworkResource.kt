package org.sea.rawg.utils

sealed class NetworkResource<out T>{
    data class Success<out T>(val data: T) : NetworkResource<T>()
    data class Error(val message: String) : NetworkResource<Nothing>()
    object Loading : NetworkResource<Nothing>()

    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    fun isLoading(): Boolean = this is Loading
}