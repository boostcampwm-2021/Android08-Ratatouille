package com.kdjj.presentation.common.extensions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun <T> Flow<T>.throttleFirst(throttleDuration: Long): Flow<T> = flow {
    var lastEmissionTime = 0L
    collect { upstream ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastEmissionTime > throttleDuration) {
            lastEmissionTime = currentTime
            emit(upstream)
        }
    }
}