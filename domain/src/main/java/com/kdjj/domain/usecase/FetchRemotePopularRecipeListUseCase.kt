package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeListRepository
import com.kdjj.domain.request.FetchRemotePopularRecipeListRequest
import javax.inject.Inject

internal class FetchRemotePopularRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository
) : UseCase<FetchRemotePopularRecipeListRequest, @JvmSuppressWildcards List<Recipe>>{

    override suspend fun invoke(request: FetchRemotePopularRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchRemotePopularRecipeListAfter(request.lastVisibleViewCount)
}