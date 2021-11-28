package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchMySearchRecipeListRequest
import com.kdjj.domain.repository.RecipeListRepository
import javax.inject.Inject

internal class FetchMySearchRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
) : ResultUseCase<FetchMySearchRecipeListRequest, @JvmSuppressWildcards List<Recipe>> {

    override suspend fun invoke(request: FetchMySearchRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchMySearchRecipeListAfter(request.keyword, request.index)
}
