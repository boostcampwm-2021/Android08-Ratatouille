package com.kdjj.domain.usecase

import com.kdjj.domain.common.flatMap
import com.kdjj.domain.model.request.UpdateUploadedRecipeRequest
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class UpdateUploadedRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val recipeImageRepository: RecipeImageRepository
) : ResultUseCase<UpdateUploadedRecipeRequest, Unit> {

    override suspend fun invoke(request: UpdateUploadedRecipeRequest): Result<Unit> {
        return recipeRepository.getLocalRecipe(request.recipeId).flatMap { recipe ->
            (listOf(recipe.imgPath) + recipe.stepList.map { it.imgPath }).map {
                coroutineScope {
                    async {
                        convertImageToRemote(it)
                    }
                }
            }.fold(Result.success(listOf<String>())) { acc, deferred ->
                acc.flatMap { imgList ->
                    deferred.await().flatMap { imgPath ->
                        Result.success(imgList + imgPath)
                    }
                }
            }.flatMap { imgList ->
                val recipeStepList = recipe.stepList.mapIndexed { idx, step ->
                    step.copy(
                        imgPath = imgList[idx + 1]
                    )
                }
                recipeRepository.uploadRecipe(
                    recipe.copy(
                        imgPath = imgList.first(),
                        stepList = recipeStepList,
                        createTime = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    private suspend fun convertImageToRemote(imgPath: String): Result<String> {
        return if (imgPath.isEmpty()) Result.success("")
        else recipeImageRepository.convertInternalUriToRemoteStorageUri(imgPath)
    }
}
