package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.GetMyRecipeRequest
import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

internal class GetMyRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ResultUseCase<GetMyRecipeRequest, Recipe> {

    override suspend fun invoke(request: GetMyRecipeRequest): Result<Recipe> =
        recipeRepository.getLocalRecipe(request.recipeId)
}
