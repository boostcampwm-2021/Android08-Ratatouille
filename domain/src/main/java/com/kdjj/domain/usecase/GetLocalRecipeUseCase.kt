package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.GetLocalRecipeRequest
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetLocalRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ResultUseCase<GetLocalRecipeRequest, Recipe> {

    override suspend fun invoke(request: GetLocalRecipeRequest): Result<Recipe> =
        recipeRepository.getLocalRecipe(request.recipeId)
}
