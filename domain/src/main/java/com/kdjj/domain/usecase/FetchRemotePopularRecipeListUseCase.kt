package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeListRepository
import com.kdjj.domain.request.FetchRemotePopularRecipeListRequset
import javax.inject.Inject

class FetchRemotePopularRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository
) : UseCase<FetchRemotePopularRecipeListRequset, @JvmSuppressWildcards List<Recipe>>{

    override suspend fun invoke(request: FetchRemotePopularRecipeListRequset): Result<List<Recipe>> =
        recipeListRepository.fetchRemotePopularRecipeListAfter(request.lastVisibleViewCount)
}