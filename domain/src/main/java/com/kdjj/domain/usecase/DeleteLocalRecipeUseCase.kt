package com.kdjj.domain.usecase

import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.model.request.DeleteLocalRecipeRequest
import javax.inject.Inject

internal class DeleteLocalRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ResultUseCase<DeleteLocalRecipeRequest, Boolean> {
    
    override suspend fun invoke(request: DeleteLocalRecipeRequest): Result<Boolean> {
        return recipeRepository.deleteLocalRecipe(request.recipe)
    }
}
