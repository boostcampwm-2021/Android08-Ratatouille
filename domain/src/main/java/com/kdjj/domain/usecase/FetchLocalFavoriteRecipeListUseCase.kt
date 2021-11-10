package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeListRepository
import com.kdjj.domain.request.FetchLocalFavoriteRecipeListRequest

internal class FetchLocalFavoriteRecipeListUseCase(
    private val recipeListRepository: RecipeListRepository
) : UseCase<FetchLocalFavoriteRecipeListRequest, @JvmSuppressWildcards List<Recipe>>{

    override suspend fun invoke(request: FetchLocalFavoriteRecipeListRequest): Result<List<Recipe>> =
        recipeListRepository.fetchLocalFavoriteRecipeListAfter(request.index)

}