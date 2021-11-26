package com.kdjj.domain.usecase

import com.kdjj.domain.common.IdGenerator
import com.kdjj.domain.model.ImageInfo
import com.kdjj.domain.model.request.UpdateLocalRecipeRequest
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeRepository
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
                .toMutableList()

            val imgInfoList = imgList.filter { it.isNotBlank() }
                .map { ImageInfo(it, idGenerator.generateId()) }

            val changedImgList = copyImageToInternal(imgInfoList)

            var i = 0
            imgList.forEachIndexed { index, s ->
                if (s.isNotBlank()) {
                    imgList[index] = changedImgList[i++]
                }
            }

            val updatedRecipeStepList = updatedRecipe.stepList.mapIndexed { i, step ->
                step.copy(
                    stepId = if (step.stepId.isBlank()) idGenerator.generateId()
                    else step.stepId,
                    imgPath = imgList[i + 1]
                )
            }

            updatedRecipe = updatedRecipe.copy(
                imgPath = imgList.first(),
                stepList = updatedRecipeStepList,
                createTime = System.currentTimeMillis()
            )

            recipeRepository.updateLocalRecipe(
                updatedRecipe,
                originRecipe.stepList.map { it.imgPath }.plus(originRecipe.imgPath)
            ).getOrThrow()
        }

    private suspend fun copyImageToInternal(imgInfoList: List<ImageInfo>): List<String> {
        if (imgInfoList.isEmpty()) return listOf()
        return if (imgInfoList.first().uri.startsWith("https://") || imgInfoList.first().uri.startsWith("gs://")) {
            imageRepository.copyRemoteImageToInternal(imgInfoList)
                .getOrThrow()
        } else {
            imageRepository.copyExternalImageToInternal(imgInfoList)
                .getOrThrow()
        }
    }
}
