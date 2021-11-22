package com.kdjj.domain.usecase

import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.model.request.DeleteRemoteRecipeRequest
import javax.inject.Inject

class DeleteRemoteRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ResultUseCase<DeleteRemoteRecipeRequest, Unit> {
    
    override suspend fun invoke(request: DeleteRemoteRecipeRequest): Result<Unit> {
        return recipeRepository.deleteRemoteRecipe(request.recipe)
    }
}
