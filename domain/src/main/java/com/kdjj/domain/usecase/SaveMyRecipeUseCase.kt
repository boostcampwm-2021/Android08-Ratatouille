package com.kdjj.domain.usecase

import com.kdjj.domain.common.IdGenerator
import com.kdjj.domain.common.flatMap
import com.kdjj.domain.model.ImageInfo
import com.kdjj.domain.model.request.SaveMyRecipeRequest
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

internal class SaveMyRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val imageRepository: RecipeImageRepository,
    private val idGenerator: IdGenerator,
) : ResultUseCase<SaveMyRecipeRequest, Unit> {

    override suspend fun invoke(request: SaveMyRecipeRequest): Result<Unit> {
        val recipe = request.recipe
        val imgList = listOf(recipe.imgPath) + recipe.stepList.map { it.imgPath }

        val imgInfoList = imgList.filter {
            it.isNotBlank()
        }.map {
            ImageInfo(it, idGenerator.generateId())
        }

        return copyImageToInternal(imgInfoList).flatMap { changedImgList ->
            var i = 0
            val totalImgList = imgList.map {
                if (it.isEmpty()) it else changedImgList[i++]
            }

            val recipeStepList = recipe.stepList.mapIndexed { idx, step ->
                step.copy(
                    stepId = idGenerator.generateId(),
                    imgPath = totalImgList[idx + 1]
                )
            }

            recipeRepository.saveMyRecipe(
                recipe.copy(
                    recipeId = idGenerator.generateId(),
                    imgPath = totalImgList.first(),
                    stepList = recipeStepList,
                    createTime = System.currentTimeMillis()
                )
            )
        }
    }

    private suspend fun copyImageToInternal(imgInfoList: List<ImageInfo>): Result<List<String>> =
        when {
            imgInfoList.isEmpty() ->
                Result.success(listOf())
            imgInfoList.first().uri.startsWith("https://") || imgInfoList.first().uri.startsWith("gs://") ->
                imageRepository.copyRemoteImageToInternal(imgInfoList)
            else ->
                imageRepository.copyExternalImageToInternal(imgInfoList)
        }
}
