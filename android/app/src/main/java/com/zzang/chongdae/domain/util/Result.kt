package com.zzang.chongdae.domain.util

typealias RootError = Error

sealed interface Result<out D, out E : RootError> {
    data class Success<out D, out E : RootError>(val data: D) : Result<D, E>

    data class Error<out D, out E : RootError>(val error: E) : Result<D, E>

    fun <R> map(transform: (D) -> R): Result<R, E> =
        when (this) {
            is Success -> Success(transform(data))
            is Error -> Error(error)
        }

    fun getOrThrow(): D =
        when (this) {
            is Success -> data
            is Error -> throw RuntimeException("Error occurred: $error")
        }
}
