package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeListRepository
import com.kdjj.domain.model.request.FetchRemoteLatestRecipeListRequest
import javax.inject.Inject

internal class FetchRemoteLatestRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository
) : UseCase<FetchRemoteLatestRecipeListRequest, @JvmSuppressWildcards List<Recipe>> {

    override suspend fun invoke(request: FetchRemoteLatestRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchRemoteLatestRecipeListAfter(request.refresh)
}
