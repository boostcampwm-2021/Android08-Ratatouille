package com.kdjj.domain.usecase

import com.kdjj.domain.common.IdGenerator
import com.kdjj.domain.model.request.SaveRecipeTempRequest
import com.kdjj.domain.repository.RecipeTempRepository
import javax.inject.Inject

internal class SaveRecipeTempUseCase @Inject constructor(
    private val tempRepository: RecipeTempRepository,
    private val idGenerator: IdGenerator
) : ResultUseCase<SaveRecipeTempRequest, Unit> {

    override suspend fun invoke(request: SaveRecipeTempRequest): Result<Unit> =
        runCatching {
            val recipe = request.recipe
            val stepList = recipe.stepList.map { step ->
                if (step.stepId.isEmpty()) step.copy(stepId = idGenerator.generateId())
                else step
            }
            return tempRepository.saveRecipeTemp(recipe.copy(stepList = stepList))
        }
}