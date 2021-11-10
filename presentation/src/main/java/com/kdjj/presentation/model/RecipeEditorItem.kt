package com.kdjj.presentation.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kdjj.domain.model.*
import com.kdjj.presentation.common.calculateSeconds
import java.lang.Exception

sealed class RecipeEditorItem {

    data class RecipeMetaModel(
        val liveTitle: MutableLiveData<String>,
        val liveRecipeTypeInt: MutableLiveData<Int>,
        val liveStuff: MutableLiveData<String>,
        val liveRecipeImgPath: MutableLiveData<String>,
        val liveRecipeType: LiveData<RecipeType>,
        val recipeId: String,
        val uploadId: String,
        var viewCount: Int = 0,
        var isFavorite: Boolean = false,

        val liveTitleState: LiveData<Boolean>,
        val liveStuffState: LiveData<Boolean>,
    ) : RecipeEditorItem()

    data class RecipeStepModel(
        val liveName: MutableLiveData<String>,
        val liveTypeInt: MutableLiveData<Int>,
        val liveDescription: MutableLiveData<String>,
        val liveImgPath: MutableLiveData<String> = MutableLiveData(""),
        val liveTimerMin: MutableLiveData<Int?>,
        val liveTimerSec: MutableLiveData<Int?>,
        val liveType: LiveData<RecipeStepType>,

        val liveNameState: LiveData<Boolean>,
        val liveDescriptionState: LiveData<Boolean>,
        val liveTimerMinState: LiveData<Boolean>,
        val liveTimerSecState: LiveData<Boolean>,

        val stepId: String
    ) : RecipeEditorItem()

    object PlusButton : RecipeEditorItem()
}

internal fun RecipeEditorItem.RecipeMetaModel.toDomain(stepModels: List<RecipeEditorItem.RecipeStepModel>) =
    Recipe(
        recipeId = recipeId,
        title = liveTitle.value ?: "",
        type= liveRecipeType.value ?: throw Exception(""),
        stuff = liveStuff.value ?: "",
        imgPath = liveRecipeImgPath.value ?: "",
        stepList = stepModels.map { it.toDomain() },
        authorId = uploadId,
        viewCount = viewCount,
        isFavorite = isFavorite,
        createTime = System.currentTimeMillis(),
        state = RecipeState.CREATE
    )

internal fun RecipeEditorItem.RecipeStepModel.toDomain() = RecipeStep(
    stepId,
    liveName.value ?: "",
    liveType.value ?: RecipeStepType.FRY,
    liveDescription.value ?: "",
    liveImgPath.value ?: "",
    calculateSeconds(
        liveTimerMin.value ?: 0,
        liveTimerSec.value ?: 0
    )
)