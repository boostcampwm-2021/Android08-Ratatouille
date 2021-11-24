package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.DeleteRecipeTempRequest
import com.kdjj.domain.repository.RecipeTempRepository
import javax.inject.Inject

class DeleteRecipeTempUseCase @Inject constructor(
    private val tempRepository: RecipeTempRepository,
) : ResultUseCase<DeleteRecipeTempRequest, Unit> {

    override suspend fun invoke(request: DeleteRecipeTempRequest): Result<Unit> =
        tempRepository.deleteRecipeTemp(request.recipe)
}