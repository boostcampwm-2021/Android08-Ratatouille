package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.GetLocalRecipeFlowRequest
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetLocalRecipeFlowUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : FlowUseCase<GetLocalRecipeFlowRequest, Recipe> {

    override fun invoke(request: GetLocalRecipeFlowRequest): Flow<Recipe> =
        recipeRepository.getLocalRecipeFlow(request.recipeId)
}
