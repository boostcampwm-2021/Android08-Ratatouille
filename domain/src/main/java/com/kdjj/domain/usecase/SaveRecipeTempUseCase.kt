package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.SaveRecipeTempRequest
import com.kdjj.domain.repository.RecipeTempRepository
import javax.inject.Inject

class SaveRecipeTempUseCase @Inject constructor(
    private val tempRepository: RecipeTempRepository,
) : ResultUseCase<SaveRecipeTempRequest, Unit> {

    override suspend fun invoke(request: SaveRecipeTempRequest): Result<Unit> =
        tempRepository.saveRecipeTemp(request.recipe)
}