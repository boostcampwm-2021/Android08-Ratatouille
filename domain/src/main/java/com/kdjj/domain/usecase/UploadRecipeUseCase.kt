package com.kdjj.domain.usecase

import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.model.request.UploadRecipeRequest
import com.kdjj.domain.repository.RecipeImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class UploadRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val recipeImageRepository: RecipeImageRepository
) : ResultUseCase<UploadRecipeRequest, Unit> {

    override suspend fun invoke(request: UploadRecipeRequest): Result<Unit> =
        runCatching {
            val recipe = request.recipe

            val imgList = listOf(recipe.imgPath)
                .plus(recipe.stepList.map { it.imgPath })
                .map {
                    CoroutineScope(Dispatchers.IO).async {
                        convertImageToRemote(it)
                    }
                }

            val recipeStepList = recipe.stepList.mapIndexed { i, step ->
                step.copy(
                    imgPath = imgList[i + 1].await()
                )
            }

            recipeRepository.uploadRecipe(
                recipe.copy(
                    imgPath = imgList.first().await(),
                    stepList = recipeStepList,
                    createTime = System.currentTimeMillis()
                )
            ).getOrThrow()

            recipeRepository.updateLocalRecipe(
                recipe.copy(
                    state = RecipeState.UPLOAD
                )
            ).getOrNull()
        }

    private suspend fun convertImageToRemote(imgPath: String): String {
        return if (imgPath.isEmpty()) ""
        else recipeImageRepository.convertInternalUriToRemoteStorageUri(imgPath)
            .getOrThrow()
    }
}
