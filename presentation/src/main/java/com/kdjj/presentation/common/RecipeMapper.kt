package com.kdjj.presentation.common

import android.content.Context
import android.provider.Settings
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeStep
import com.kdjj.domain.model.RecipeStepType
import com.kdjj.presentation.model.RecipeItem
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.Exception
import javax.inject.Inject

class RecipeMapper @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun recipeItemListToRecipe(recipeItemList: List<RecipeItem>): Recipe {
        val recipeMetaModel = recipeItemList[0] as RecipeItem.RecipeMetaModel
        val recipeStepList = recipeItemList.subList(1, recipeItemList.lastIndex)

        return Recipe(
            recipeId = generateId(),
            title = recipeMetaModel.liveTitle.value ?: "",
            type= recipeMetaModel.liveRecipeType.value ?: throw Exception(""),
            stuff = recipeMetaModel.liveStuff.value ?: "",
            imgPath = recipeMetaModel.liveRecipeImgPath.value ?: "",
            stepList = recipeStepList.map { recipeStepModelToRecipeStep(it as RecipeItem.RecipeStepModel) },
            uploaderId = recipeMetaModel.uploadId,
            viewCount = recipeMetaModel.viewCount,
            isFavorite = recipeMetaModel.isFavorite,
            createTime = System.currentTimeMillis()
        )
    }

    fun recipeStepModelToRecipeStep(recipeStepModel: RecipeItem.RecipeStepModel): RecipeStep {
        return RecipeStep(
            generateId(),
            recipeStepModel.liveName.value ?: "",
            recipeStepModel.liveType.value ?: RecipeStepType.FRY,
            recipeStepModel.liveDescription.value ?: "",
            recipeStepModel.liveImgPath.value ?: "",
            calculateSeconds(
                recipeStepModel.liveTimerMin.value ?: 0,
                recipeStepModel.liveTimerSec.value ?: 0
            )
        )
    }

    private fun calculateSeconds(min: Int, sec: Int): Int{
        return min*60 + sec
    }

    private fun generateId() = getDeviceId() + System.currentTimeMillis()

    private fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}