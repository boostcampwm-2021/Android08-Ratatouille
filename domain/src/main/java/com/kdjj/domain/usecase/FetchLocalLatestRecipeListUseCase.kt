package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeListRepository
import com.kdjj.domain.model.request.FetchLocalLatestRecipeListRequest
import javax.inject.Inject

internal class FetchLocalLatestRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
) : ResultUseCase<FetchLocalLatestRecipeListRequest, @JvmSuppressWildcards List<Recipe>> {

    override suspend fun invoke(request: FetchLocalLatestRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchLocalLatestRecipeListAfter(request.index)
}
