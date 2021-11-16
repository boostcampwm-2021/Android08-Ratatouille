package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.GetLocalRecipeRequest
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : UseCase<GetLocalRecipeRequest, Flow<Recipe>> {
    
    override suspend fun invoke(request: GetLocalRecipeRequest): Result<Flow<Recipe>> =
        recipeRepository.getLocalRecipeFlow(request.recipeId)
}
