package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeListRepository
import com.kdjj.domain.request.EmptyRequest
import javax.inject.Inject

class FetchRemoteLatestRecipeListUseCase @Inject constructor(
    private val recipeListRepository: RecipeListRepository
) : UseCase<EmptyRequest, @JvmSuppressWildcards List<Recipe>> {

    override suspend fun invoke(request: EmptyRequest): Result<List<Recipe>> =
        recipeListRepository.fetchRemoteLatestRecipeList()
}