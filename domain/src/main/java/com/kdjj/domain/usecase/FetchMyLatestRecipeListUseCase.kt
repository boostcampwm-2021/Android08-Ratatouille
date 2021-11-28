package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchMyLatestRecipeListRequest
import com.kdjj.domain.repository.RecipeListRepository
import javax.inject.Inject

internal class FetchMyLatestRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
) : ResultUseCase<FetchMyLatestRecipeListRequest, @JvmSuppressWildcards List<Recipe>> {

    override suspend fun invoke(request: FetchMyLatestRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchMyLatestRecipeListAfter(request.index)
}
