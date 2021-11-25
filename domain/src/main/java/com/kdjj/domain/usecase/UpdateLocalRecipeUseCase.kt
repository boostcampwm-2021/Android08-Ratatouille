package com.kdjj.domain.usecase

import com.kdjj.domain.common.IdGenerator
import com.kdjj.domain.model.request.UpdateLocalRecipeRequest
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class UpdateLocalRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val imageRepository: RecipeImageRepository,
    private val idGenerator: IdGenerator,
) : ResultUseCase<UpdateLocalRecipeRequest, Unit> {

    override suspend fun invoke(request: UpdateLocalRecipeRequest): Result<Unit> =
        runCatching {
            val originRecipe = recipeRepository.getLocalRecipe(request.updatedRecipe.recipeId).getOrThrow()
            var updatedRecipe = request.updatedRecipe

            val imgList = listOf(updatedRecipe.imgPath)
                .plus(updatedRecipe.stepList.map { it.imgPath })
                .map {
                    coroutineScope {
                        async {
                            copyImageToInternal(it)
                        }
                    }
                }

            val updatedRecipeStepList = updatedRecipe.stepList.mapIndexed { i, step ->
                step.copy(
                    stepId = if (step.stepId.isBlank()) idGenerator.generateId()
                    else step.stepId,
                    imgPath = imgList[i + 1].await()
                )
            }

            updatedRecipe = updatedRecipe.copy(
                imgPath = imgList.first().await(),
                stepList = updatedRecipeStepList,
                createTime = System.currentTimeMillis()
            )

            recipeRepository.updateLocalRecipe(
                updatedRecipe,
                originRecipe.stepList.map { it.imgPath }.plus(originRecipe.imgPath)
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
