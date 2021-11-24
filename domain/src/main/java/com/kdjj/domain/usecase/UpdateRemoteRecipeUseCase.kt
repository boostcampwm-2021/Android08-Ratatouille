package com.kdjj.domain.usecase

import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.model.request.UpdateRemoteRecipeRequest
import com.kdjj.domain.repository.RecipeImageRepository
import javax.inject.Inject

class UpdateRemoteRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val recipeImageRepository: RecipeImageRepository
) : ResultUseCase<UpdateRemoteRecipeRequest, Unit> {
    
    override suspend fun invoke(request: UpdateRemoteRecipeRequest): Result<Unit> =
        runCatching {
            val recipe = recipeRepository.getLocalRecipe(request.recipeId).getOrThrow()
            val recipeImageUri = when (recipe.imgPath.isNotEmpty()) {
                true -> {
                    recipeImageRepository.convertInternalUriToRemoteStorageUri(recipe.imgPath)
                        .getOrThrow()
                }
                false -> ""
            }
            val recipeStepList = recipe.stepList.map { step ->
                val stepImageUri = when (step.imgPath.isNotEmpty()) {
                    true -> {
                        recipeImageRepository.convertInternalUriToRemoteStorageUri(step.imgPath)
                            .getOrThrow()
                    }
                    false -> ""
                }
                step.copy(imgPath = stepImageUri)
            }
            recipeRepository.uploadRecipe(
                recipe.copy(
                    imgPath = recipeImageUri,
                    stepList = recipeStepList,
                    createTime = System.currentTimeMillis()
                )
            ).getOrThrow()
        }
}
