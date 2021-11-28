package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.DeleteUploadedRecipeRequest
import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

class DeleteUploadedRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ResultUseCase<DeleteUploadedRecipeRequest, Unit> {

    override suspend fun invoke(request: DeleteUploadedRecipeRequest): Result<Unit> {
        return recipeRepository.deleteOthersRecipe(request.recipe)
    }
}
