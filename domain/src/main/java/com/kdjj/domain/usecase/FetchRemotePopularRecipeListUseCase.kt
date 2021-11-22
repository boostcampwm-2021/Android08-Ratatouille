package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeListRepository
import com.kdjj.domain.model.request.FetchRemotePopularRecipeListRequest
import javax.inject.Inject

internal class FetchRemotePopularRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository
) : ResultUseCase<FetchRemotePopularRecipeListRequest, @JvmSuppressWildcards List<Recipe>>{

    override suspend fun invoke(request: FetchRemotePopularRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchRemotePopularRecipeListAfter(request.refresh)
}
