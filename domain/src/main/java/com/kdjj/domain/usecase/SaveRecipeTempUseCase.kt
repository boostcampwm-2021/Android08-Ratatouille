package com.kdjj.domain.usecase

import com.kdjj.domain.common.IdGenerator
import com.kdjj.domain.model.ImageInfo
import com.kdjj.domain.model.request.SaveRecipeTempRequest
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeTempRepository
import javax.inject.Inject

internal class SaveRecipeTempUseCase @Inject constructor(
    private val tempRepository: RecipeTempRepository,
    private val imageRepository: RecipeImageRepository,
    private val idGenerator: IdGenerator
) : ResultUseCase<SaveRecipeTempRequest, Unit> {

    override suspend fun invoke(request: SaveRecipeTempRequest): Result<Unit> =
        runCatching {
            val recipe = request.recipe
            val imgPathList = listOf(recipe.imgPath) + recipe.stepList.map { it.imgPath }
            val imgInfoList = imgPathList.filter { it.isNotEmpty() }
                .map { ImageInfo(it, idGenerator.generateId()) }

            val copiedImgPathList = copyImageToInternal(imgInfoList)

            var i = 0
            val totalImgList = imgPathList.map {
                if (it.isEmpty()) it else copiedImgPathList[i++]
            }

            val stepList = recipe.stepList.mapIndexed { idx, step ->
                if (step.stepId.isEmpty()) step.copy(
                    stepId = idGenerator.generateId(),
                    imgPath = totalImgList[idx + 1]
                )
                else step.copy(
                    imgPath = totalImgList[idx + 1]
                )
            }

            return tempRepository.saveRecipeTemp(
                recipe.copy(
                    imgPath = totalImgList.first(),
                    stepList = stepList
                )
            )
        }

    private suspend fun copyImageToInternal(imgInfoList: List<ImageInfo>): List<String> {
        if (imgInfoList.isEmpty()) return listOf()
        return imageRepository.copyExternalImageToInternal(imgInfoList)
            .getOrThrow()
    }
}