package com.kdjj.presentation.viewmodel.recipeeditor

import android.net.Uri
import androidx.lifecycle.*
import com.kdjj.domain.model.RecipeStepType
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.request.EmptyRequest
import com.kdjj.domain.request.RecipeRequest
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.*
import com.kdjj.presentation.model.RecipeEditorItem
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
    private val recipeMapper: RecipeMapper,
    private val idGenerator: IdGenerator
) : ViewModel() {

    private var _liveRecipeItemList = MutableLiveData<List<RecipeEditorItem>>()
    val liveRecipeItemList: LiveData<List<RecipeEditorItem>> get() = _liveRecipeItemList

    val stepTypes = RecipeStepType.values()
    val liveStringStepTypes = MutableLiveData(stepTypes.map { it.name })

    private val _liveRecipeTypes = MutableLiveData<List<RecipeType>>()
    val liveStringRecipeTypes: LiveData<List<String>> get() = _liveRecipeTypes.switchMap { list ->
        MutableLiveData(list.map { it.title })
    }

    init {
        _liveRecipeItemList.value = listOf(
            createEmptyRecipeMetaModel(),
            createEmptyRecipeStepModel(),
            RecipeEditorItem.PlusButton
        )
        fetchRecipeTypes()
    }

    private fun fetchRecipeTypes() {
        viewModelScope.launch {
            recipeTypesUseCase(EmptyRequest())
                .onSuccess {
                    _liveRecipeTypes.value = it
                }
                .onFailure {
                    // todo
                }
        }
    }

    fun setRecipeImg(uri: Uri) {
        _liveRecipeItemList.value?.let {
            (it[0] as RecipeEditorItem.RecipeMetaModel).liveRecipeImgPath.value = uri.path
        }
    }

    fun addRecipeStep() {
        _liveRecipeItemList.value?.let {
            _liveRecipeItemList.value = it.subList(0, it.lastIndex) + createEmptyRecipeStepModel() + it.last()
        }
    }

    private fun createEmptyRecipeMetaModel(): RecipeEditorItem.RecipeMetaModel {
        val liveTitle = MutableLiveData<String>("")
        val liveStuff = MutableLiveData<String>("")
        val liveRecipeImgPath = MutableLiveData<String>()
        val liveCategoryPosition = MutableLiveData(0)
        return RecipeEditorItem.RecipeMetaModel(
            liveTitle = liveTitle,
            liveStuff = liveStuff,
            liveRecipeImgPath = liveRecipeImgPath,
            liveRecipeTypeInt = liveCategoryPosition,
            liveRecipeType = liveCategoryPosition.switchMap { MutableLiveData(_liveRecipeTypes.value?.get(it) ?: throw Exception()) },

            liveStuffState = liveStuff.switchMap { MutableLiveData(recipeValidator.validateStuff(it)) },
            liveTitleState = liveTitle.switchMap { MutableLiveData(recipeValidator.validateTitle(it)) },

            recipeId = idGenerator.generateId(),
            uploadId = idGenerator.getDeviceId()
        )
    }

    private fun createEmptyRecipeStepModel(): RecipeEditorItem.RecipeStepModel {
        val liveName =  MutableLiveData("")
        val liveDescription = MutableLiveData("")
        val liveTimerMin = MutableLiveData(0)
        val liveTimerSec = MutableLiveData(0)
        val liveTypeInt = MutableLiveData(0)
        return RecipeEditorItem.RecipeStepModel(
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

            stepId = idGenerator.generateId()
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
                is RecipeEditorItem.RecipeMetaModel -> {
                    if (it.liveTitleState.value != true || it.liveStuffState.value != true) {
                        return false
                    }
                }
                is RecipeEditorItem.RecipeStepModel -> {
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