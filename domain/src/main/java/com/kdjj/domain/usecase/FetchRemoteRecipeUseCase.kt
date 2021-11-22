package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchRemoteRecipeRequest
import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

internal class FetchRemoteRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) :ResultUseCase<FetchRemoteRecipeRequest, @JvmSuppressWildcards Recipe> {

    override suspend fun invoke(request: FetchRemoteRecipeRequest): Result<Recipe> =
        recipeRepository.fetchRemoteRecipe(request.recipeID)
}
