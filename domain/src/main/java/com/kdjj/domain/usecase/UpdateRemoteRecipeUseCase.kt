package com.kdjj.domain.usecase

import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.model.request.UpdateRemoteRecipeRequest
import com.kdjj.domain.repository.RecipeImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class UpdateRemoteRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val recipeImageRepository: RecipeImageRepository
) : ResultUseCase<UpdateRemoteRecipeRequest, Unit> {
    
    override suspend fun invoke(request: UpdateRemoteRecipeRequest): Result<Unit> =
        runCatching {
            val recipe = recipeRepository.getLocalRecipe(request.recipeId).getOrThrow()

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
        }

    private suspend fun convertImageToRemote(imgPath: String): String {
        return if (imgPath.isEmpty()) ""
        else recipeImageRepository.convertInternalUriToRemoteStorageUri(imgPath)
            .getOrThrow()
    }
}
