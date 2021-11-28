package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchOthersLatestRecipeListRequest
import com.kdjj.domain.repository.RecipeListRepository
import javax.inject.Inject

internal class FetchOthersLatestRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository
) : ResultUseCase<FetchOthersLatestRecipeListRequest, @JvmSuppressWildcards List<Recipe>> {

    override suspend fun invoke(request: FetchOthersLatestRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchOthersLatestRecipeListAfter(request.refresh)
}
