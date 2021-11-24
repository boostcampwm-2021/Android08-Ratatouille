package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.EmptyRequest
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipeUpdateFlowUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : FlowUseCase<EmptyRequest, Int> {

    override fun invoke(request: EmptyRequest): Flow<Int> =
        recipeRepository.getRecipeUpdateFlow()
}
