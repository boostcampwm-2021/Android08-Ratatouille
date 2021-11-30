package com.kdjj.domain.usecase

import com.kdjj.domain.common.IdGenerator
import com.kdjj.domain.common.flatMap
import com.kdjj.domain.model.ImageInfo
import com.kdjj.domain.model.request.UpdateMyRecipeRequest
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

internal class UpdateMyRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val imageRepository: RecipeImageRepository,
    private val idGenerator: IdGenerator,
) : ResultUseCase<UpdateMyRecipeRequest, Unit> {

    override suspend fun invoke(request: UpdateMyRecipeRequest): Result<Unit> {
        return recipeRepository.getLocalRecipe(request.updatedRecipe.recipeId).flatMap { originRecipe ->
            val updatedRecipe = request.updatedRecipe
            val imgList = listOf(updatedRecipe.imgPath) + updatedRecipe.stepList.map { it.imgPath }

            val imgInfoList = imgList.filter {
                it.isNotBlank()
            }.map {
                ImageInfo(it, idGenerator.generateId())
            }

            copyImageToInternal(imgInfoList).flatMap { changedImgList ->
                var i = 0
                val totalImgList = imgList.map {
                    if (it.isEmpty()) it else changedImgList[i++]
                }

                val stepList = updatedRecipe.stepList.mapIndexed { idx, step ->
                    step.copy(
                        stepId = if (step.stepId.isBlank()) idGenerator.generateId()
                        else step.stepId,
                        imgPath = totalImgList[idx + 1]
                    )
                }

                recipeRepository.updateMyRecipe(
                    updatedRecipe.copy(
                        imgPath = totalImgList.first(),
                        stepList = stepList,
                        createTime = System.currentTimeMillis()
                    ),
                    originRecipe.stepList.map { it.imgPath }.plus(originRecipe.imgPath)
                )
            }
        }
    }

    private suspend fun copyImageToInternal(imgInfoList: List<ImageInfo>): Result<List<String>> {
        return when {
            imgInfoList.isEmpty() ->
                Result.success(listOf())
            imgInfoList.first().uri.startsWith("https://") || imgInfoList.first().uri.startsWith("gs://") ->
                imageRepository.copyRemoteImageToInternal(imgInfoList)
            else ->
                imageRepository.copyExternalImageToInternal(imgInfoList)
        }
    }
}
