package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.IncreaseOthersRecipeViewCountRequest
import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

class IncreaseOthersRecipeViewCountUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ResultUseCase<IncreaseOthersRecipeViewCountRequest, Unit> {

    override suspend fun invoke(request: IncreaseOthersRecipeViewCountRequest): Result<Unit> {
        return recipeRepository.increaseOthersRecipeViewCount(request.recipe)
    }
}
