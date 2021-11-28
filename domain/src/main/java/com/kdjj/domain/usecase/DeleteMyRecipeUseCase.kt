package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.DeleteMyRecipeRequest
import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

internal class DeleteMyRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ResultUseCase<DeleteMyRecipeRequest, Boolean> {

    override suspend fun invoke(request: DeleteMyRecipeRequest): Result<Boolean> {
        return recipeRepository.deleteMyRecipe(request.recipe)
    }
}
