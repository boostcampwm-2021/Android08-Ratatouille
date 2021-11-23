package com.kdjj.domain.usecase

import com.kdjj.domain.common.IdGenerator
import com.kdjj.domain.model.request.UpdateLocalRecipeRequest
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

internal class UpdateLocalRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val imageRepository: RecipeImageRepository,
    private val idGenerator: IdGenerator,
) : ResultUseCase<UpdateLocalRecipeRequest, Boolean>{

    override suspend fun invoke(request: UpdateLocalRecipeRequest): Result<Boolean> {
        recipeRepository.getLocalRecipe(request.updatedRecipe.recipeId)
        return Result.success(true)
    }
}