package com.kdjj.domain.usecase

import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.request.EmptyRequest
import javax.inject.Inject

class FetchRecipeTypesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : UseCase<EmptyRequest, List<RecipeType>> {

    override suspend fun invoke(request: EmptyRequest): Result<List<RecipeType>> =
        recipeRepository.fetchRecipeTypes()
}