package com.kdjj.presentation.common

import android.content.Context
import android.provider.Settings
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.RecipeStep
import com.kdjj.domain.model.RecipeStepType
import com.kdjj.presentation.model.RecipeItem
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.Exception
import javax.inject.Inject

class RecipeMapper @Inject constructor() {

    fun recipeItemListToRecipe(recipeItemList: List<RecipeItem>): Recipe {
        val recipeMetaModel = recipeItemList[0] as RecipeItem.RecipeMetaModel
        val recipeStepList = recipeItemList.subList(1, recipeItemList.lastIndex)

        return Recipe(
            recipeId = recipeMetaModel.recipeId,
            title = recipeMetaModel.liveTitle.value ?: "",
            type= recipeMetaModel.liveRecipeType.value ?: throw Exception(""),
            stuff = recipeMetaModel.liveStuff.value ?: "",
            imgPath = recipeMetaModel.liveRecipeImgPath.value ?: "",
            stepList = recipeStepList.map { recipeStepModelToRecipeStep(it as RecipeItem.RecipeStepModel) },
            authorId = recipeMetaModel.uploadId,
            viewCount = recipeMetaModel.viewCount,
            isFavorite = recipeMetaModel.isFavorite,
            createTime = System.currentTimeMillis(),
            state = RecipeState.CREATE
        )
    }

    fun recipeStepModelToRecipeStep(recipeStepModel: RecipeItem.RecipeStepModel): RecipeStep {
        return RecipeStep(
            recipeStepModel.stepId,
            recipeStepModel.liveName.value ?: "",
            recipeStepModel.liveType.value ?: RecipeStepType.FRY,
            recipeStepModel.liveDescription.value ?: "",
            recipeStepModel.liveImgPath.value ?: "",
            Utils.calculateSeconds(
                recipeStepModel.liveTimerMin.value ?: 0,
                recipeStepModel.liveTimerSec.value ?: 0
            )
        )
    }
}