package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.GetMyRecipeRequest
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetMyRecipeFlowUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : FlowUseCase<GetMyRecipeRequest, Recipe> {

    override fun invoke(request: GetMyRecipeRequest): Flow<Recipe> =
        recipeRepository.getMyRecipeFlow(request.recipeId)
}
