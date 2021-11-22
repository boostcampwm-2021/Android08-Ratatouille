package com.kdjj.domain.usecase

import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.model.request.IncreaseRemoteRecipeViewCountRequest
import javax.inject.Inject

class IncreaseRemoteRecipeViewCountUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ResultUseCase<IncreaseRemoteRecipeViewCountRequest, Unit> {
    
    override suspend fun invoke(request: IncreaseRemoteRecipeViewCountRequest): Result<Unit> {
        return recipeRepository.increaseRemoteRecipeViewCount(request.recipe)
    }
}
