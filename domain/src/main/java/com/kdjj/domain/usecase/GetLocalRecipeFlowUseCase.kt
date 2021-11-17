package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.GetLocalRecipeFlowRequest
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalRecipeFlowUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : UseCase<GetLocalRecipeFlowRequest, @JvmSuppressWildcards Flow<Recipe>> {
    
    override suspend fun invoke(request: GetLocalRecipeFlowRequest): Result<Flow<Recipe>> =
        recipeRepository.getLocalRecipeFlow(request.recipeId)
}
