package com.kdjj.presentation.viewmodel.recipeeditor

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.kdjj.domain.model.RecipeStepType
import com.kdjj.presentation.model.RecipeStepModel
import javax.inject.Inject

class RecipeEditorViewModel @Inject constructor(
) : ViewModel() {

    //livedata naming convention
    val liveTitle = MutableLiveData<String>()
    val liveTitleState = liveTitle.switchMap {
        MutableLiveData(validateTitle(it))
    }

    val liveCategoryPosition = MutableLiveData<Int>(0) // spinner position

    val liveStuff = MutableLiveData<String>()
    val liveStuffState = liveStuff.switchMap {
        MutableLiveData(validateStuff(it))
    }

    val liveRecipeImgPath = MutableLiveData<String>()

    fun setRecipeImg(uri: Uri) {
        liveRecipeImgPath.value = uri.path
    }

    private val _liveStepList = MutableLiveData<List<RecipeStepModel>>()
    val liveStepList: LiveData<List<RecipeStepModel>> = _liveStepList

    fun addRecipeStep() {
        _liveStepList.value?.let {
            _liveStepList.value = it.plus(RecipeStepModel())
        }
    }

    fun removeRecipeStep(position: Int) {
        _liveStepList.value?.let {
            if (it.size <= 1) _liveStepList.value = emptyList()
            else {
                if (position == 0) _liveStepList.value = it.subList(1, it.size)
                else _liveStepList.value = it.subList(position, it.size)
            }
        }
    }

    private fun validateTitle(title: String): Boolean {
        // to-do
        return true
    }

    private fun validateStuff(stuff: String): Boolean {
        // to-do
        return true
    }
}