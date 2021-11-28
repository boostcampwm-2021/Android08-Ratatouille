package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchMyFavoriteRecipeListRequest
import com.kdjj.domain.repository.RecipeListRepository
import javax.inject.Inject

internal class FetchMyFavoriteRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository
) : ResultUseCase<FetchMyFavoriteRecipeListRequest, @JvmSuppressWildcards List<Recipe>> {

    override suspend fun invoke(request: FetchMyFavoriteRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchMyFavoriteRecipeListAfter(request.index)
}
