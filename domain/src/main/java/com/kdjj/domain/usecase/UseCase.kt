package com.kdjj.domain.usecase

import com.kdjj.domain.request.Request

interface UseCase<R: Request, T> {
    suspend operator fun invoke(request: R): Result<T>
}
