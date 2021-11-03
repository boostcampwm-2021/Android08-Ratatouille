package com.kdjj.domain.usecase

import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.request.RecipeRequest
import javax.inject.Inject

class SaveRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
): UseCase<RecipeRequest, Boolean> {

    override suspend fun invoke(request: RecipeRequest): Result<Boolean> =
        recipeRepository.saveRecipe(request.recipe)
}