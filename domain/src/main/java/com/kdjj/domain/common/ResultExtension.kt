package com.kdjj.domain.common

import java.lang.Exception


inline fun <T> Result<T>.errorMap(
    transform: (Throwable) -> Exception
): Result<T> {
    return when (val exception = exceptionOrNull()){
        null -> this
        else -> { Result.failure(transform.invoke(exception))}
    }
}

inline fun <T, R> Result<T>.flatMap(
    transform: (T) -> Result<R>
): Result<R> {
    return when (val exception = exceptionOrNull()) {
        null -> transform(getOrThrow())
        else -> Result.failure(exception)
    }
}

inline fun <T> Result<T>.handleWith(
    transform: (Throwable) -> Result<T>
): Result<T> {
    return when (val exception = exceptionOrNull()) {
        null -> this
        else -> transform(exception)
    }
}