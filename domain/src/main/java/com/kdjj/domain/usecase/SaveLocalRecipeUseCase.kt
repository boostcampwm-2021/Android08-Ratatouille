package com.kdjj.domain.usecase

import com.kdjj.domain.common.IdGenerator
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.model.request.SaveLocalRecipeRequest
import javax.inject.Inject

internal class SaveLocalRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val imageRepository: RecipeImageRepository,
    private val idGenerator: IdGenerator,
) : ResultUseCase<SaveLocalRecipeRequest, Boolean> {

    override suspend fun invoke(request: SaveLocalRecipeRequest): Result<Boolean> =
        kotlin.runCatching {
            val recipe = request.recipe
            val recipeImageUri = when (recipe.imgPath.isNotEmpty()) {
                true -> {
                    if (recipe.imgPath.startsWith("https://") || recipe.imgPath.startsWith("gs://")) {
                        imageRepository.copyRemoteImageToInternal(recipe.imgPath, idGenerator.generateId())
                            .getOrThrow()
                    } else {
                        imageRepository.copyExternalImageToInternal(recipe.imgPath, idGenerator.generateId())
                            .getOrThrow()
                    }
                }
                false -> ""
            }
            val recipeStepList = recipe.stepList.map { step ->
                val stepImageUri = when (step.imgPath.isNotEmpty()) {
                    true -> {
                        if (recipe.imgPath.startsWith("https://") || recipe.imgPath.startsWith("gs://")) {
                            imageRepository.copyRemoteImageToInternal(step.imgPath, idGenerator.generateId())
                                .getOrThrow()
                        } else {
                            imageRepository.copyExternalImageToInternal(step.imgPath, idGenerator.generateId())
                                .getOrThrow()
                        }
                    }
                    false -> ""
                }
                step.copy(imgPath = stepImageUri)
            }

            recipeRepository.saveLocalRecipe(
                recipe.copy(
                    imgPath = recipeImageUri,
                    stepList = recipeStepList,
                    createTime = System.currentTimeMillis()
                )
            ).getOrThrow()
        }
}
