package com.kdjj.domain.usecase

import com.kdjj.domain.common.IdGenerator
import com.kdjj.domain.model.ImageInfo
import com.kdjj.domain.model.request.SaveLocalRecipeRequest
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

internal class SaveLocalRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val imageRepository: RecipeImageRepository,
    private val idGenerator: IdGenerator,
) : ResultUseCase<SaveLocalRecipeRequest, Boolean> {

    override suspend fun invoke(request: SaveLocalRecipeRequest): Result<Boolean> =
        kotlin.runCatching {
            val recipe = request.recipe
            val imgList = listOf(recipe.imgPath)
                .plus(recipe.stepList.map { it.imgPath })
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

            val recipeStepList = recipe.stepList.mapIndexed { i, step ->
                step.copy(
                    stepId = idGenerator.generateId(),
                    imgPath = imgList[i + 1]
                )
            }

            recipeRepository.saveLocalRecipe(
                recipe.copy(
                    recipeId = idGenerator.generateId(),
                    imgPath = imgList.first(),
                    stepList = recipeStepList,
                    createTime = System.currentTimeMillis()
                )
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
