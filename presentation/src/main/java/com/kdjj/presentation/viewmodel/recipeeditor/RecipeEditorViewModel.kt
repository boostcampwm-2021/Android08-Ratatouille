package com.kdjj.presentation.viewmodel.recipeeditor

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.kdjj.presentation.common.RecipeStepValidator
import com.kdjj.presentation.common.RecipeValidator
import com.kdjj.presentation.model.RecipeStepModel
import javax.inject.Inject

class RecipeEditorViewModel @Inject constructor(
    private val recipeValidator: RecipeValidator,
    private val recipeStepValidator: RecipeStepValidator,
) : ViewModel() {

    //livedata naming convention
    val liveTitle = MutableLiveData<String>()
    val liveTitleState = liveTitle.switchMap {
        MutableLiveData(recipeValidator.validateTitle(it))
    }

    val liveCategoryPosition = MutableLiveData<Int>(0) // spinner position

    val liveStuff = MutableLiveData<String>()
    val liveStuffState = liveStuff.switchMap {
        MutableLiveData(recipeValidator.validateStuff(it))
    }

    val liveRecipeImgPath = MutableLiveData<String>()

    fun setRecipeImg(uri: Uri) {
        liveRecipeImgPath.value = uri.path
    }

    private val _liveStepList = MutableLiveData<List<RecipeStepModel>>(listOf())
    val liveStepList: LiveData<List<RecipeStepModel>> = _liveStepList

    fun addRecipeStep() {
        _liveStepList.value?.let {
            _liveStepList.value = it.plus(createEmptyRecipeStepModel())
        }
    }

    fun createEmptyRecipeStepModel(): RecipeStepModel {
        val liveName =  MutableLiveData("")
        val liveDescription = MutableLiveData("")
        val liveTimerMin = MutableLiveData(0)
        val liveTimerSec = MutableLiveData(0)
        return RecipeStepModel(
            liveName = liveName,
            liveDescription = liveDescription,
            liveTimerMin = liveTimerMin,
            liveTimerSec = liveTimerSec,

            liveNameState = liveName.switchMap { MutableLiveData(recipeStepValidator.validateName(it)) },
            liveDescriptionState = liveDescription.switchMap { MutableLiveData(recipeStepValidator.validateDescription(it)) },
            liveTimerMinState = liveTimerMin.switchMap { MutableLiveData(recipeStepValidator.validateMinutes(it)) },
            liveTimerSecState = liveTimerSec.switchMap { MutableLiveData(recipeStepValidator.validateSeconds(it)) },
        )
    }

    fun removeRecipeStep(position: Int) {
        _liveStepList.value?.let {
            if (it.size <= 1) _liveStepList.value = emptyList()
            else {
                if (position == 0) _liveStepList.value = it.subList(1, it.size)
                else if (position == it.lastIndex) _liveStepList.value = it.subList(0, it.lastIndex)
                else _liveStepList.value = it.subList(0, position) + it.subList(position + 1, it.size)
            }
        }
    }
}