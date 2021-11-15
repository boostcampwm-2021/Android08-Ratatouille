package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.Request

interface UseCase<R: Request, T> {
    suspend operator fun invoke(request: R): Result<T>
}
