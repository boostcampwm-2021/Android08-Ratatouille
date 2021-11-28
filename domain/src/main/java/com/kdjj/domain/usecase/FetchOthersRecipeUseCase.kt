package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchOthersRecipeRequest
import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

internal class FetchOthersRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ResultUseCase<FetchOthersRecipeRequest, @JvmSuppressWildcards Recipe> {

    override suspend fun invoke(request: FetchOthersRecipeRequest): Result<Recipe> =
        recipeRepository.fetchOthersRecipe(request.recipeId)
}
