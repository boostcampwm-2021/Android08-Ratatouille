package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchOthersPopularRecipeListRequest
import com.kdjj.domain.repository.RecipeListRepository
import javax.inject.Inject

internal class FetchOthersPopularRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository
) : ResultUseCase<FetchOthersPopularRecipeListRequest, @JvmSuppressWildcards List<Recipe>> {

    override suspend fun invoke(request: FetchOthersPopularRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchOthersPopularRecipeListAfter(request.refresh)
}
