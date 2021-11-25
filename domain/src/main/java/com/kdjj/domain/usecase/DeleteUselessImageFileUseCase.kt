package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.EmptyRequest
import com.kdjj.domain.repository.RecipeImageRepository
import javax.inject.Inject

internal class DeleteUselessImageFileUseCase @Inject constructor(
    private val recipeImageRepository: RecipeImageRepository,
) : ResultUseCase<EmptyRequest, Unit> {

    override suspend fun invoke(request: EmptyRequest): Result<Unit> =
        recipeImageRepository.deleteUselessImages()
}