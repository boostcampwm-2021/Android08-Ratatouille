package com.kdjj.domain.usecase

import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.model.request.SaveLocalRecipeRequest
import javax.inject.Inject

internal class SaveLocalRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val imageRepository: RecipeImageRepository
) : UseCase<SaveLocalRecipeRequest, Boolean> {
    
    override suspend fun invoke(request: SaveLocalRecipeRequest): Result<Boolean> =
        kotlin.runCatching {
            val recipe = request.recipe
            val recipeImageUri = when (recipe.imgPath.isNotEmpty()) {
                true -> {
                    imageRepository.copyExternalImageToInternal(recipe.imgPath, recipe.recipeId)
                        .getOrThrow()
                }
                false -> ""
            }
            val recipeStepList = recipe.stepList.map { step ->
                val stepImageUri = when (step.imgPath.isNotEmpty()) {
                    true -> {
                        imageRepository.copyExternalImageToInternal(step.imgPath, step.stepId)
                            .getOrThrow()
                    }
                    false -> ""
                }
                step.copy(imgPath = stepImageUri)
            }
            
            recipeRepository.saveLocalRecipe(
                recipe.copy(
                    imgPath = recipeImageUri,
                    stepList = recipeStepList
                )
            ).getOrThrow()
        }
}
