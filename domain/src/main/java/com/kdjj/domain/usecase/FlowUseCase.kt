package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.Request
import kotlinx.coroutines.flow.Flow

interface FlowUseCase<R : Request, T> {
    operator fun invoke(request: R): Flow<T>
}
