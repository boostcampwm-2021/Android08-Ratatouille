package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeListRepository
import com.kdjj.domain.model.request.FetchRemoteSearchRecipeListRequest
import javax.inject.Inject

internal class FetchRemoteSearchRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
): UseCase<FetchRemoteSearchRecipeListRequest, @JvmSuppressWildcards List<Recipe>>{

    override suspend fun invoke(request: FetchRemoteSearchRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchRemoteSearchRecipeListAfter(request.keyword, request.lastVisibleTitle)
}
