package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchLocalTitleRecipeListRequest
import com.kdjj.domain.repository.RecipeListRepository

class FetchLocalTitleRecipeListUseCase(
    private val recipeListRepository: RecipeListRepository,
) : UseCase<FetchLocalTitleRecipeListRequest, @JvmSuppressWildcards List<Recipe>> {

    override suspend fun invoke(request: FetchLocalTitleRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchLocalTitleRecipeListAfter(request.index)
}