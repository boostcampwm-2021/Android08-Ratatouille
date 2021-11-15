package com.kdjj.domain.usecase

import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.model.request.UpdateRemoteRecipeRequest
import javax.inject.Inject

class UpdateRemoteRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : UseCase<UpdateRemoteRecipeRequest, Unit> {
    
    override suspend fun invoke(request: UpdateRemoteRecipeRequest): Result<Unit> {
        // TODO : workmanager를 이용한 백그라운드 처리
        return recipeRepository.uploadRecipe(request.recipe)
    }
}
