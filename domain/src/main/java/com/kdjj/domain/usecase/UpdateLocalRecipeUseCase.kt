package com.kdjj.domain.usecase

import com.kdjj.domain.common.IdGenerator
import com.kdjj.domain.model.request.UpdateLocalRecipeRequest
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeRepository
import java.lang.Exception
import javax.inject.Inject

internal class UpdateLocalRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val imageRepository: RecipeImageRepository,
    private val idGenerator: IdGenerator,
) : ResultUseCase<UpdateLocalRecipeRequest, Unit>{

    override suspend fun invoke(request: UpdateLocalRecipeRequest): Result<Unit> =
        runCatching {
            val originRecipe = recipeRepository.getLocalRecipe(request.updatedRecipe.recipeId).getOrThrow()
            var updatedRecipe = request.updatedRecipe
            val updatedRecipeImgUri = when (updatedRecipe.imgPath.isNotEmpty()) {
                true -> {
                    if (updatedRecipe.imgPath.startsWith("https://") || updatedRecipe.imgPath.startsWith("gs://")) {
                        imageRepository.copyRemoteImageToInternal(updatedRecipe.imgPath, idGenerator.generateId())
                            .getOrThrow()
                    } else {
                        imageRepository.copyExternalImageToInternal(updatedRecipe.imgPath, idGenerator.generateId())
                            .getOrThrow()
                    }
                }
                false -> ""
            }
            val updatedRecipeStepList = updatedRecipe.stepList.map { step ->
                val stepImageUri = when (step.imgPath.isNotEmpty()) {
                    true -> {
                        if (updatedRecipe.imgPath.startsWith("https://") || updatedRecipe.imgPath.startsWith("gs://")) {
                            imageRepository.copyRemoteImageToInternal(step.imgPath, idGenerator.generateId())
                                .getOrThrow()
                        } else {
                            imageRepository.copyExternalImageToInternal(step.imgPath, idGenerator.generateId())
                                .getOrThrow()
                        }
                    }
                    false -> ""
                }
                step.copy(imgPath = stepImageUri)
            }

            updatedRecipe = updatedRecipe.copy(
                imgPath = updatedRecipeImgUri,
                stepList = updatedRecipeStepList,
                createTime = System.currentTimeMillis()
            )

            recipeRepository.updateLocalRecipe(
                updatedRecipe,
                originRecipe.stepList.map { it.imgPath }.plus(originRecipe.imgPath)
            ).getOrThrow()

            //upload logic
            //내부 uri -> fb uri
            //내부 uri 리턴
        }

}