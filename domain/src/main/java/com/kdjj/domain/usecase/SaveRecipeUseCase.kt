package com.kdjj.domain.usecase

import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.request.SaveRecipeRequest
import javax.inject.Inject

internal class SaveRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : UseCase<SaveRecipeRequest, Boolean> {
    
    override suspend fun invoke(request: SaveRecipeRequest): Result<Boolean> =
        recipeRepository.saveRecipe(request.recipe)
}
