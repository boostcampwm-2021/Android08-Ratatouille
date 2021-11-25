package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeListRepository
import com.kdjj.domain.model.request.FetchLocalFavoriteRecipeListRequest
import javax.inject.Inject

internal class FetchLocalFavoriteRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository
) : ResultUseCase<FetchLocalFavoriteRecipeListRequest, @JvmSuppressWildcards List<Recipe>>{

    override suspend fun invoke(request: FetchLocalFavoriteRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchLocalFavoriteRecipeListAfter(request.index)
}
