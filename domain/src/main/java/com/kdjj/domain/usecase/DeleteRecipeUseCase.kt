package com.kdjj.domain.usecase

import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.request.DeleteRecipeRequest
import javax.inject.Inject

internal class DeleteRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : UseCase<DeleteRecipeRequest, Boolean> {
    
    override suspend fun invoke(request: DeleteRecipeRequest): Result<Boolean> {
        return recipeRepository.deleteRecipe(request.recipe)
    }
}
