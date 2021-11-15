package com.kdjj.data.common

import java.lang.Exception


inline fun <T> Result<T>.errorMap(
    transform: (Throwable) -> Exception
): Result<T> {
    return when (val exception = exceptionOrNull()){
        null -> this
        else -> { Result.failure(transform.invoke(exception))}
    }
}