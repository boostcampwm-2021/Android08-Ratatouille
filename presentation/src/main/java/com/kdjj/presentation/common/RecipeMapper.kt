package com.kdjj.presentation.common

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.RecipeStep
import com.kdjj.domain.model.RecipeStepType
import com.kdjj.presentation.model.RecipeEditorItem
import java.lang.Exception
import javax.inject.Inject

class RecipeMapper @Inject constructor() {

    fun recipeItemListToRecipe(recipeEditorItemList: List<RecipeEditorItem>): Recipe {
        val recipeMetaModel = recipeEditorItemList[0] as RecipeEditorItem.RecipeMetaModel
        val recipeStepList = recipeEditorItemList.subList(1, recipeEditorItemList.lastIndex)

        return Recipe(
            recipeId = recipeMetaModel.recipeId,
            title = recipeMetaModel.liveTitle.value ?: "",
            type= recipeMetaModel.liveRecipeType.value ?: throw Exception(""),
            stuff = recipeMetaModel.liveStuff.value ?: "",
            imgPath = recipeMetaModel.liveRecipeImgPath.value ?: "",
            stepList = recipeStepList.map { recipeStepModelToRecipeStep(it as RecipeEditorItem.RecipeStepModel) },
            authorId = recipeMetaModel.uploadId,
            viewCount = recipeMetaModel.viewCount,
            isFavorite = recipeMetaModel.isFavorite,
            createTime = System.currentTimeMillis(),
            state = RecipeState.CREATE
        )
    }

    fun recipeStepModelToRecipeStep(recipeEditorStepModel: RecipeEditorItem.RecipeStepModel): RecipeStep {
        return RecipeStep(
            recipeEditorStepModel.stepId,
            recipeEditorStepModel.liveName.value ?: "",
            recipeEditorStepModel.liveType.value ?: RecipeStepType.FRY,
            recipeEditorStepModel.liveDescription.value ?: "",
            recipeEditorStepModel.liveImgPath.value ?: "",
            Utils.calculateSeconds(
                recipeEditorStepModel.liveTimerMin.value ?: 0,
                recipeEditorStepModel.liveTimerSec.value ?: 0
            )
        )
    }
}