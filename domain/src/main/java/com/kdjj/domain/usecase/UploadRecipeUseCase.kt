package com.kdjj.domain.usecase

import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.model.request.UploadRecipeRequest
import javax.inject.Inject

class UploadRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : UseCase<UploadRecipeRequest, Unit> {
    
    override suspend fun invoke(request: UploadRecipeRequest): Result<Unit> {
        return recipeRepository.uploadRecipe(request.recipe)
    }
}
