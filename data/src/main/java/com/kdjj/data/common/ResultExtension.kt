package com.kdjj.data.common

import java.lang.Exception


inline fun <T> Result<T>.errorMap(
    transform: (Throwable?) -> Exception
): Result<T> {
    return when {
        isSuccess -> this
        else -> { Result.failure(transform.invoke(exceptionOrNull()))}
    }
}