package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.EmptyRequest
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipeUpdateStateUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
): UseCase<EmptyRequest, @kotlin.jvm.JvmSuppressWildcards Flow<Int>> {

    override suspend fun invoke(request: EmptyRequest): Result<Flow<Int>> {
        return recipeRepository.getRecipeUpdateState()
    }
}