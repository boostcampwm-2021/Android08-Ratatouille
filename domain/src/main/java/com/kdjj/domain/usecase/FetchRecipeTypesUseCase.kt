package com.kdjj.domain.usecase

import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.repository.RecipeTypeRepository
import com.kdjj.domain.request.EmptyRequest
import javax.inject.Inject

class FetchRecipeTypesUseCase @Inject constructor(
	private val recipeTypeRepository: RecipeTypeRepository
) : UseCase<EmptyRequest, @JvmSuppressWildcards List<RecipeType>> {
	
	override suspend fun invoke(request: EmptyRequest): Result<List<RecipeType>> {
		val remoteResult = recipeTypeRepository.fetchRemoteRecipeTypes()
		return when {
			remoteResult.isSuccess -> {
				remoteResult.also {
					recipeTypeRepository.saveRecipeType(it.getOrThrow())
				}
			}
			else -> {
				recipeTypeRepository.fetchLocalRecipeTypes()
			}
		}
	}
}
