package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchOthersSearchRecipeListRequest
import com.kdjj.domain.repository.RecipeListRepository
import javax.inject.Inject

internal class FetchOthersSearchRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
) : ResultUseCase<FetchOthersSearchRecipeListRequest, @JvmSuppressWildcards List<Recipe>> {

    override suspend fun invoke(request: FetchOthersSearchRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchOthersSearchRecipeListAfter(request.keyword, request.refresh)
}
