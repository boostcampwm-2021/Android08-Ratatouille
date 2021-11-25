package com.kdjj.domain.usecase

import com.kdjj.domain.common.IdGenerator
import com.kdjj.domain.model.request.SaveLocalRecipeRequest
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class SaveLocalRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val imageRepository: RecipeImageRepository,
    private val idGenerator: IdGenerator,
) : ResultUseCase<SaveLocalRecipeRequest, Boolean> {

    override suspend fun invoke(request: SaveLocalRecipeRequest): Result<Boolean> =
        kotlin.runCatching {
            val recipe = request.recipe
            // imgList always size >= 2
            val imgList = listOf(recipe.imgPath)
                .plus(recipe.stepList.map { it.imgPath })
                .map {
                    coroutineScope {
                        async {
                            copyImageToInternal(it)
                        }
                    }
                }

            val recipeStepList = recipe.stepList.mapIndexed { i, step ->
                step.copy(
                    stepId = idGenerator.generateId(),
                    imgPath = imgList[i + 1].await()
                )
            }

            recipeRepository.saveLocalRecipe(
                recipe.copy(
                    recipeId = idGenerator.generateId(),
                    imgPath = imgList.first().await(),
                    stepList = recipeStepList,
                    createTime = System.currentTimeMillis()
                )
            ).getOrThrow()
        }

    private suspend fun copyImageToInternal(imgPath: String): String {
        if (imgPath.isEmpty()) return ""
        return if (imgPath.startsWith("https://") || imgPath.startsWith("gs://")) {
            imageRepository.copyRemoteImageToInternal(imgPath, idGenerator.generateId())
                .getOrThrow()
        } else {
            imageRepository.copyExternalImageToInternal(imgPath, idGenerator.generateId())
                .getOrThrow()
        }
    }
}
