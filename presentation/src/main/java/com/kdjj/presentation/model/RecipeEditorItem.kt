package com.kdjj.presentation.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.kdjj.domain.model.*
import com.kdjj.presentation.common.IdGenerator
import com.kdjj.presentation.common.RecipeStepValidator
import com.kdjj.presentation.common.RecipeValidator
import com.kdjj.presentation.common.calculateSeconds
import java.lang.Exception

sealed class RecipeEditorItem {

    data class RecipeMetaModel(
        val liveTitle: MutableLiveData<String>,
        val liveRecipeTypeInt: MutableLiveData<Int>,
        val liveStuff: MutableLiveData<String>,
        val liveRecipeImgPath: MutableLiveData<String>,
        val recipeId: String,
        val uploadId: String,
        var viewCount: Int = 0,
        var isFavorite: Boolean = false,

        val liveTitleState: LiveData<Boolean>,
        val liveStuffState: LiveData<Boolean>,
    ) : RecipeEditorItem() {

        companion object {

            fun create(
                idGenerator: IdGenerator,
                recipeTypes: List<RecipeType>,
                recipeValidator: RecipeValidator
            ): RecipeMetaModel {
                val liveTitle = MutableLiveData<String>("")
                val liveStuff = MutableLiveData<String>("")
                val liveRecipeImgPath = MutableLiveData<String>("")
                val liveCategoryPosition = MutableLiveData<Int>(0)
                return RecipeMetaModel(
                    liveTitle = liveTitle,
                    liveStuff = liveStuff,
                    liveRecipeImgPath = liveRecipeImgPath,
                    liveRecipeTypeInt = liveCategoryPosition,

                    liveStuffState = liveStuff.switchMap { MutableLiveData(recipeValidator.validateStuff(it)) },
                    liveTitleState = liveTitle.switchMap { MutableLiveData(recipeValidator.validateTitle(it)) },

                    recipeId = idGenerator.generateId(),
                    uploadId = idGenerator.getDeviceId()
                )
            }
        }
    }

    data class RecipeStepModel(
        val liveName: MutableLiveData<String>,
        val liveTypeInt: MutableLiveData<Int>,
        val liveDescription: MutableLiveData<String>,
        val liveImgPath: MutableLiveData<String> = MutableLiveData(""),
        val liveTimerMin: MutableLiveData<Int?>,
        val liveTimerSec: MutableLiveData<Int?>,

        val liveNameState: LiveData<Boolean>,
        val liveDescriptionState: LiveData<Boolean>,
        val liveTimerMinState: LiveData<Boolean>,
        val liveTimerSecState: LiveData<Boolean>,

        val stepId: String
    ) : RecipeEditorItem() {

        companion object {

            fun create(
                idGenerator: IdGenerator,
                recipeStepValidator: RecipeStepValidator
            ): RecipeStepModel {
                val stepTypes = RecipeStepType.values()

                val liveName =  MutableLiveData("")
                val liveDescription = MutableLiveData("")
                val liveTimerMin = MutableLiveData<Int?>(0)
                val liveTimerSec = MutableLiveData<Int?>(0)
                val liveTypeInt = MutableLiveData<Int>(0)
                return RecipeStepModel(
                    liveName = liveName,
                    liveDescription = liveDescription,
                    liveTimerMin = liveTimerMin,
                    liveTimerSec = liveTimerSec,
                    liveTypeInt = liveTypeInt,

                    liveNameState = liveName.switchMap { MutableLiveData(recipeStepValidator.validateName(it)) },
                    liveDescriptionState = liveDescription.switchMap { MutableLiveData(recipeStepValidator.validateDescription(it)) },
                    liveTimerMinState = liveTimerMin.switchMap { MutableLiveData(recipeStepValidator.validateMinutes(it)) },
                    liveTimerSecState = liveTimerSec.switchMap { MutableLiveData(recipeStepValidator.validateSeconds(it)) },

                    stepId = idGenerator.generateId()
                )
            }
        }
    }

    object PlusButton : RecipeEditorItem()
}

internal fun RecipeEditorItem.RecipeMetaModel.toDomain(
    stepModels: List<RecipeEditorItem.RecipeStepModel>,
    typeList: List<RecipeType>
) = Recipe(
        recipeId = recipeId,
        title = liveTitle.value ?: "",
        type= typeList[liveRecipeTypeInt.value ?: throw Exception()],
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
    RecipeStepType.values()[liveTypeInt.value ?: 0],
    liveDescription.value ?: "",
    liveImgPath.value ?: "",
    calculateSeconds(
        liveTimerMin.value ?: 0,
        liveTimerSec.value ?: 0
    )
)

internal fun Recipe.toPresentation(
    recipeValidator: RecipeValidator,
    recipeTypes: List<RecipeType>,
    recipeStepValidator: RecipeStepValidator
): Pair<RecipeEditorItem.RecipeMetaModel, List<RecipeEditorItem.RecipeStepModel>> {
    val liveTitle = MutableLiveData<String>(title)
    val liveStuff = MutableLiveData<String>(stuff)
    val liveRecipeImgPath = MutableLiveData<String>(imgPath)
    val liveCategoryPosition = MutableLiveData<Int>(recipeTypes.indexOfFirst { it.id == type.id })
    return RecipeEditorItem.RecipeMetaModel(
        liveTitle = liveTitle,
        liveStuff = liveStuff,
        liveRecipeImgPath = liveRecipeImgPath,
        liveRecipeTypeInt = liveCategoryPosition,

        liveStuffState = liveStuff.switchMap { MutableLiveData(recipeValidator.validateStuff(it)) },
        liveTitleState = liveTitle.switchMap { MutableLiveData(recipeValidator.validateTitle(it)) },

        recipeId = recipeId,
        uploadId = authorId
    ) to stepList.map { it.toPresentation(recipeStepValidator) }
}

internal fun RecipeStep.toPresentation(
    recipeStepValidator: RecipeStepValidator
): RecipeEditorItem.RecipeStepModel {
    val stepTypes = RecipeStepType.values()

    val liveName =  MutableLiveData(name)
    val liveDescription = MutableLiveData(description)
    val liveTimerMin = MutableLiveData<Int?>(seconds / 60)
    val liveTimerSec = MutableLiveData<Int?>(seconds % 60)
    val liveTypeInt = MutableLiveData<Int>(type.ordinal)
    return RecipeEditorItem.RecipeStepModel(
        liveName = liveName,
        liveDescription = liveDescription,
        liveTimerMin = liveTimerMin,
        liveTimerSec = liveTimerSec,
        liveTypeInt = liveTypeInt,

        liveNameState = liveName.switchMap {
            MutableLiveData(recipeStepValidator.validateName(it))
        },
        liveDescriptionState = liveDescription.switchMap {
            MutableLiveData(recipeStepValidator.validateDescription(it))
        },
        liveTimerMinState = liveTimerMin.switchMap {
            MutableLiveData(recipeStepValidator.validateMinutes(it))
        },
        liveTimerSecState = liveTimerSec.switchMap {
            MutableLiveData(recipeStepValidator.validateSeconds(it))
        },

        stepId = stepId
    )
}