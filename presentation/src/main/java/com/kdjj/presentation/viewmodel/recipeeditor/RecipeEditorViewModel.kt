package com.kdjj.presentation.viewmodel.recipeeditor

import androidx.lifecycle.*
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeStepType
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.request.EmptyRequest
import com.kdjj.domain.request.SaveRecipeRequest
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.*
import com.kdjj.presentation.model.RecipeEditorItem
import com.kdjj.presentation.model.toDomain
import com.kdjj.presentation.model.toPresentation
import com.kdjj.presentation.view.dialog.ConfirmDialogBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RecipeEditorViewModel @Inject constructor(
    private val recipeValidator: RecipeValidator,
    private val recipeStepValidator: RecipeStepValidator,
    private val recipeSaveUseCase: UseCase<SaveRecipeRequest, Boolean>,
    private val recipeTypesUseCase: UseCase<EmptyRequest, List<RecipeType>>,
    private val idGenerator: IdGenerator,
) : ViewModel() {

    private lateinit var recipeMetaModel: RecipeEditorItem.RecipeMetaModel
    private var recipeStepModelList = listOf<RecipeEditorItem.RecipeStepModel>()

    private val _liveRecipeItemList = MutableLiveData<List<RecipeEditorItem>>()
    val liveRecipeItemList: LiveData<List<RecipeEditorItem>> get() = _liveRecipeItemList

    val stepTypes = RecipeStepType.values()

    private val _liveRecipeTypes = MutableLiveData<List<RecipeType>>()
    val liveRecipeTypes: LiveData<List<RecipeType>> get() = _liveRecipeTypes

    private val _liveImgTarget = MutableLiveData<RecipeEditorItem?>()
    val liveImgTarget: LiveData<RecipeEditorItem?> get() = _liveImgTarget

    private val _liveRegisterHasPressed = MutableLiveData(false)
    val liveRegisterHasPressed: LiveData<Boolean> get() = _liveRegisterHasPressed

    private val _liveSaveResult = MutableLiveData<Boolean?>()
    val liveSaveResult: LiveData<Boolean?> get() = _liveSaveResult

    fun initializeWith(recipe: Recipe?) {
        viewModelScope.launch {
            recipeTypesUseCase(EmptyRequest)
                .onSuccess { recipeTypes ->
                    _liveRecipeTypes.value = recipeTypes
                    recipe?.let {
                        val (metaModel, stepList) =
                            recipe.toPresentation(recipeValidator, recipeTypes, recipeStepValidator)
                        recipeMetaModel = metaModel
                        recipeStepModelList = stepList
                    } ?: run {
                        recipeMetaModel = RecipeEditorItem.RecipeMetaModel.create(
                            idGenerator, recipeTypes, recipeValidator
                        )
                        recipeStepModelList = listOf(RecipeEditorItem.RecipeStepModel.create(
                            idGenerator, recipeStepValidator
                        ))
                    }
                    notifyStepListChange()
                }
                .onFailure {

                }
        }
    }

    private fun notifyStepListChange() {
        _liveRecipeItemList.value =
            listOf(recipeMetaModel) + recipeStepModelList + RecipeEditorItem.PlusButton
    }

    fun startSelectImage(model: RecipeEditorItem) {
        _liveImgTarget.value = model
    }

    fun setImage(uri: String) {
        liveImgTarget.value?.let { model ->
            when (model) {
                is RecipeEditorItem.RecipeMetaModel ->
                    model.liveRecipeImgPath.value = uri
                is RecipeEditorItem.RecipeStepModel ->
                    model.liveImgPath.value = uri
            }
        }
        _liveImgTarget.value = null
    }

    fun cancelSelectImage() {
        _liveImgTarget.value = null
    }

    fun setImageEmpty() {
        liveImgTarget.value?.let { model ->
            when (model) {
                is RecipeEditorItem.RecipeMetaModel -> model.liveRecipeImgPath.value = null
                is RecipeEditorItem.RecipeStepModel -> model.liveImgPath.value = null
            }
        }
        _liveImgTarget.value = null
    }

    fun addRecipeStep() {
        recipeStepModelList = recipeStepModelList +
                RecipeEditorItem.RecipeStepModel.create(idGenerator, recipeStepValidator)
        notifyStepListChange()
    }

    fun removeRecipeStep(position: Int) {
        recipeStepModelList = recipeStepModelList.subList(0, position - 1) +
                recipeStepModelList.subList(position, recipeStepModelList.size)
        notifyStepListChange()
    }

    fun changeRecipeStepPosition(from: Int, to: Int) {
        recipeStepModelList = recipeStepModelList.toMutableList().apply {
            set(from - 1, set(to - 1, get(from - 1)))
        }
        notifyStepListChange()
    }

    fun saveRecipe() {
        _liveRegisterHasPressed.value = true
        if (isRecipeValid()) {
            viewModelScope.launch {
                recipeSaveUseCase(
                    SaveRecipeRequest(recipeMetaModel.toDomain(recipeStepModelList, liveRecipeTypes.value ?: emptyList()))
                ).onSuccess {
                    println(1)
                    _liveSaveResult.value = true
                }.onFailure {
                    println(2)
                    _liveSaveResult.value = false
                }
            }
        }
    }

    fun resetResultState() {
        _liveSaveResult.value = null
    }

    private fun isRecipeValid(): Boolean {
        if (
            recipeMetaModel.liveTitleState.value != true ||
            recipeMetaModel.liveStuffState.value != true
        ) {
            println("meta false")
            return false
        }

        recipeStepModelList.forEach { stepModel ->
            if (stepModel.liveNameState.value != true ||
                stepModel.liveDescriptionState.value != true ||
                stepModel.liveTimerMinState.value != true ||
                stepModel.liveTimerSecState.value != true
            ) {
                println("step false")
                return false
            }
        }

        return true
    }
}
