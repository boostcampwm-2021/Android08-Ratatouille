package com.kdjj.presentation.viewmodel.recipeeditor

import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.RecipeStepType
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.model.request.*
import com.kdjj.domain.usecase.FlowUseCase
import com.kdjj.domain.usecase.ResultUseCase
import com.kdjj.presentation.common.*
import com.kdjj.presentation.model.RecipeEditorItem
import com.kdjj.presentation.model.toDomain
import com.kdjj.presentation.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RecipeEditorViewModel @Inject constructor(
    private val recipeValidator: RecipeValidator,
    private val recipeStepValidator: RecipeStepValidator,
    private val saveRecipeUseCase: ResultUseCase<SaveLocalRecipeRequest, Boolean>,
    private val fetchRecipeTypesUseCase: ResultUseCase<EmptyRequest, List<RecipeType>>,
    private val getLocalRecipeFlowUseCase: FlowUseCase<GetLocalRecipeFlowRequest, Recipe>,
    private val updateRemoteRecipeUseCase: ResultUseCase<UpdateRemoteRecipeRequest, Unit>,
    private val updateLocalRecipeUseCase: ResultUseCase<UpdateLocalRecipeRequest, Unit>,
    private val idGenerator: IdGenerator,
    private val workManager: WorkManager
) : ViewModel() {

    private lateinit var recipeMetaModel: RecipeEditorItem.RecipeMetaModel
    private val _liveStepModelList = MutableLiveData<List<RecipeEditorItem.RecipeStepModel>>()

    val liveRecipeItemList: LiveData<List<RecipeEditorItem>> = _liveStepModelList.map {
        listOf(recipeMetaModel) + it + RecipeEditorItem.PlusButton
    }

    val stepTypes = RecipeStepType.values()
    private val _liveRecipeTypes = MutableLiveData<List<RecipeType>>()
    val liveRecipeTypes: LiveData<List<RecipeType>> get() = _liveRecipeTypes

    private val _liveImgTarget = MutableLiveData<RecipeEditorItem?>()
    val liveImgTarget: LiveData<RecipeEditorItem?> get() = _liveImgTarget

    private val _liveRegisterHasPressed = MutableLiveData(false)
    val liveRegisterHasPressed: LiveData<Boolean> get() = _liveRegisterHasPressed

    private val _liveLoading = MutableLiveData(false)
    val liveLoading: LiveData<Boolean> get() = _liveLoading

    private var isInitialized = false

    private val _liveMoveToPosition = MutableLiveData<Int>()
    val liveMoveToPosition: LiveData<Int> get() = _liveMoveToPosition

    private var isEditing = false

    private val _eventRecipeEditor = MutableLiveData<Event<RecipeEditorEvent>>()
    val eventRecipeEditor: LiveData<Event<RecipeEditorEvent>> get() = _eventRecipeEditor

    sealed class RecipeEditorEvent {
        class SaveResult(val isSuccess: Boolean) : RecipeEditorEvent()
        object Error : RecipeEditorEvent()
    }

    fun initializeWith(recipeId: String?) {
        if (isInitialized) return

        _liveLoading.value = true
        viewModelScope.launch {
            fetchRecipeTypesUseCase(EmptyRequest)
                .onSuccess { recipeTypes ->
                    _liveRecipeTypes.value = recipeTypes
                    recipeId?.let {
                        val recipeFlow =
                            getLocalRecipeFlowUseCase(GetLocalRecipeFlowRequest(recipeId))
                        val recipe = recipeFlow.first()
                        val (metaModel, stepList) =
                            recipe.toPresentation(recipeValidator, recipeTypes, recipeStepValidator)
                        recipeMetaModel = metaModel
                        _liveStepModelList.value = stepList
                        isEditing = true

                    } ?: run {
                        recipeMetaModel = RecipeEditorItem.RecipeMetaModel.create(
                            idGenerator, recipeValidator
                        )
                        _liveStepModelList.value = listOf(
                            RecipeEditorItem.RecipeStepModel.create(
                                idGenerator, recipeStepValidator
                            )
                        )
                    }
                }
                .onFailure {
                    _eventRecipeEditor.value = Event(RecipeEditorEvent.Error)
                }
            _liveLoading.value = false
        }
        isInitialized = true
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
        _liveStepModelList.value = (_liveStepModelList.value ?: listOf()) +
                RecipeEditorItem.RecipeStepModel.create(idGenerator, recipeStepValidator)
        _liveMoveToPosition.value = (_liveStepModelList.value?.size ?: 0) + 2
    }

    fun removeRecipeStep(position: Int) {
        _liveStepModelList.value?.let { modelList ->
            _liveStepModelList.value = modelList.subList(0, position - 1) +
                    modelList.subList(position, modelList.size)
        }
    }

    fun changeRecipeStepPosition(from: Int, to: Int) {
        _liveStepModelList.value?.let { modelList ->
            _liveStepModelList.value = modelList.toMutableList().apply {
                set(from - 1, set(to - 1, get(from - 1)))
            }
        }
    }

    fun saveRecipe() {
        _liveRegisterHasPressed.value = true
        if (isRecipeValid()) {
            _liveLoading.value = true
            viewModelScope.launch {
                val recipe = recipeMetaModel.toDomain(
                    _liveStepModelList.value ?: listOf(),
                    liveRecipeTypes.value ?: emptyList()
                )
                val res = if (isEditing) {
                    updateLocalRecipeUseCase(UpdateLocalRecipeRequest(recipe))
                } else {
                    saveRecipeUseCase(SaveLocalRecipeRequest(recipe))
                }
                res.onSuccess {
                    if (recipe.state == RecipeState.UPLOAD) registerUploadTask(recipe.recipeId)
                    _eventRecipeEditor.value =
                        Event(RecipeEditorEvent.SaveResult(true))
                }.onFailure {
                    _eventRecipeEditor.value =
                        Event(RecipeEditorEvent.SaveResult(false))

                }
                _liveLoading.value = false
            }
        }
    }

    private fun registerUploadTask(updatedRecipeId: String) {
        val updateWorkerRequest = OneTimeWorkRequestBuilder<RecipeUploadWorker>().apply {
            setInputData(
                workDataOf(
                    UPDATED_RECIPE_ID to updatedRecipeId
                )
            )
        }.build()
        workManager.enqueue(updateWorkerRequest)
    }

    private fun isRecipeValid(): Boolean {
        if (
            recipeMetaModel.liveTitleState.value != true ||
            recipeMetaModel.liveStuffState.value != true
        ) {
            _liveMoveToPosition.value = 0
            return false
        }

        _liveStepModelList.value?.forEachIndexed { i, stepModel ->
            if (stepModel.liveNameState.value != true ||
                stepModel.liveDescriptionState.value != true ||
                stepModel.liveTimerMinState.value != true ||
                stepModel.liveTimerSecState.value != true
            ) {
                _liveMoveToPosition.value = i + 1
                return false
            }
        }

        return true
    }
}
