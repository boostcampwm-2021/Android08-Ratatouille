package com.kdjj.presentation.viewmodel.recipeeditor

import android.net.Uri
import androidx.lifecycle.*
import com.kdjj.domain.model.RecipeStepType
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.request.EmptyRequest
import com.kdjj.domain.request.RecipeRequest
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.RecipeMapper
import com.kdjj.presentation.common.RecipeStepValidator
import com.kdjj.presentation.common.RecipeValidator
import com.kdjj.presentation.model.RecipeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RecipeEditorViewModel @Inject constructor(
    private val recipeValidator: RecipeValidator,
    private val recipeStepValidator: RecipeStepValidator,
    private val recipeSaveUseCase: UseCase<RecipeRequest, Boolean>,
    private val recipeTypesUseCase: UseCase<EmptyRequest, List<RecipeType>>,
    private val recipeMapper: RecipeMapper
) : ViewModel() {

    private var _liveRecipeItemList = MutableLiveData<List<RecipeItem>>()
    val liveRecipeItemList = _liveRecipeItemList

    val stepTypes = RecipeStepType.values()
    lateinit var recipeTypes : List<RecipeType>

    init {
        _liveRecipeItemList.value = listOf(createEmptyRecipeMetaModel(), RecipeItem.PlusButton)
    }

    fun fetchRecipeTypes() {
        viewModelScope.launch {
            val res = recipeTypesUseCase.invoke(EmptyRequest())
            res.onSuccess {
                recipeTypes = it
            }
            res.onFailure {
                // todo
            }
        }
    }

    fun setRecipeImg(uri: Uri) {
        _liveRecipeItemList.value?.let {
            (it[0] as RecipeItem.RecipeMetaModel).liveRecipeImgPath.value = uri.path
        }
    }

    fun addRecipeStep() {
        _liveRecipeItemList.value?.let {
            _liveRecipeItemList.value = it.subList(0, it.lastIndex) + createEmptyRecipeStepModel() + it.last()
        }
    }

    private fun createEmptyRecipeMetaModel(): RecipeItem.RecipeMetaModel {
        val liveTitle = MutableLiveData<String>("")
        val liveStuff = MutableLiveData<String>("")
        val liveRecipeImgPath = MutableLiveData<String>()
        val liveCategoryPosition = MutableLiveData(0)
        return RecipeItem.RecipeMetaModel(
            liveTitle = liveTitle,
            liveStuff = liveStuff,
            liveRecipeImgPath = liveRecipeImgPath,
            liveRecipeTypeInt = liveCategoryPosition,
            liveRecipeType = liveCategoryPosition.switchMap { MutableLiveData(recipeTypes[it]) },

            liveStuffState = liveStuff.switchMap { MutableLiveData(recipeValidator.validateStuff(it)) },
            liveTitleState = liveTitle.switchMap { MutableLiveData(recipeValidator.validateTitle(it)) }
        )
    }

    private fun createEmptyRecipeStepModel(): RecipeItem.RecipeStepModel {
        val liveName =  MutableLiveData("")
        val liveDescription = MutableLiveData("")
        val liveTimerMin = MutableLiveData(0)
        val liveTimerSec = MutableLiveData(0)
        val liveTypeInt = MutableLiveData(0)
        return RecipeItem.RecipeStepModel(
            liveName = liveName,
            liveDescription = liveDescription,
            liveTimerMin = liveTimerMin,
            liveTimerSec = liveTimerSec,
            liveTypeInt = liveTypeInt,
            liveType = liveTypeInt.switchMap { MutableLiveData(stepTypes[it]) },

            liveNameState = liveName.switchMap { MutableLiveData(recipeStepValidator.validateName(it)) },
            liveDescriptionState = liveDescription.switchMap { MutableLiveData(recipeStepValidator.validateDescription(it)) },
            liveTimerMinState = liveTimerMin.switchMap { MutableLiveData(recipeStepValidator.validateMinutes(it)) },
            liveTimerSecState = liveTimerSec.switchMap { MutableLiveData(recipeStepValidator.validateSeconds(it)) },
        )
    }

    fun removeRecipeStep(position: Int) {
        if (position > 0) {
            _liveRecipeItemList.value?.let {
                _liveRecipeItemList.value = it.subList(0, position) + it.subList(position + 1, it.size)
            }
        }
    }

    fun changeRecipeStepPosition(from: Int, to: Int) {
        _liveRecipeItemList.value?.let {
            if (it.isNotEmpty()) {
                Collections.swap(it, from, to)
                _liveRecipeItemList.value = it
            }
        }
    }

    fun saveRecipe() {
        viewModelScope.launch {
            liveRecipeItemList.value?.let {
                val res = recipeSaveUseCase.invoke( RecipeRequest(recipeMapper.recipeItemListToRecipe(it)))
                res.onSuccess { /*화면 이동*/ }
                res.onFailure {  }
            }
        }
    }

    private fun checkAllValidate(): Boolean {
        // check recipe meta
        _liveRecipeItemList.value?.forEach {
            when (it) {
                is RecipeItem.RecipeMetaModel -> {
                    if (it.liveTitleState.value != true || it.liveStuffState.value != true) {
                        return false
                    }
                }
                is RecipeItem.RecipeStepModel -> {
                    if (it.liveNameState.value != true || it.liveDescriptionState.value != true ||
                        it.liveTimerMinState.value != true || it.liveTimerSecState.value != true) {
                        return false
                    }
                }
                else -> {}
            }
        } ?: return false

        return true
    }
}