package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchMyTitleRecipeListRequest
import com.kdjj.domain.repository.RecipeListRepository
import javax.inject.Inject

class FetchMyTitleRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
) : ResultUseCase<FetchMyTitleRecipeListRequest, @JvmSuppressWildcards List<Recipe>> {

    override suspend fun invoke(request: FetchMyTitleRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchMyTitleRecipeListAfter(request.index)
}
