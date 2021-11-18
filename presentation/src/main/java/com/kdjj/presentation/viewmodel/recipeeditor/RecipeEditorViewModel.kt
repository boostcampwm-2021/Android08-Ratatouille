package com.kdjj.presentation.viewmodel.recipeeditor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeStepType
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.model.request.EmptyRequest
import com.kdjj.domain.model.request.GetLocalRecipeFlowRequest
import com.kdjj.domain.model.request.SaveLocalRecipeRequest
import com.kdjj.domain.model.request.UpdateRemoteRecipeRequest
import com.kdjj.domain.usecase.UpdateRemoteRecipeUseCase
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.common.IdGenerator
import com.kdjj.presentation.common.RecipeStepValidator
import com.kdjj.presentation.common.RecipeValidator
import com.kdjj.presentation.model.RecipeEditorItem
import com.kdjj.presentation.model.toDomain
import com.kdjj.presentation.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RecipeEditorViewModel @Inject constructor(
    private val recipeValidator: RecipeValidator,
    private val recipeStepValidator: RecipeStepValidator,
    private val saveRecipeUseCase: UseCase<SaveLocalRecipeRequest, Boolean>,
    private val fetchRecipeTypesUseCase: UseCase<EmptyRequest, List<RecipeType>>,
    private val getLocalRecipeFlowUseCase: UseCase<GetLocalRecipeFlowRequest, Flow<Recipe>>,
    private val updateRemoteRecipeUseCase: UseCase<UpdateRemoteRecipeRequest, Unit>,
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

    private val _eventSaveResult = MutableLiveData<Event<Boolean>>()
    val eventSaveResult: LiveData<Event<Boolean>> get() = _eventSaveResult

    private val _liveLoading = MutableLiveData(false)
    val liveLoading: LiveData<Boolean> get() = _liveLoading

    private var isInitialized = false

    private val _liveMoveToPosition = MutableLiveData<Int>()
    val liveMoveToPosition: LiveData<Int> get() = _liveMoveToPosition

    private var isEditing = false

    private val _eventError = MutableLiveData<Event<Unit>>()
    val eventError: LiveData<Event<Unit>> get() = _eventError
    
    fun initializeWith(recipeId: String?) {
        if (isInitialized) return
        
        _liveLoading.value = true
        viewModelScope.launch {
            fetchRecipeTypesUseCase(EmptyRequest)
                .onSuccess { recipeTypes ->
                    _liveRecipeTypes.value = recipeTypes
                    recipeId?.let {
                        getLocalRecipeFlowUseCase(GetLocalRecipeFlowRequest(recipeId))
                            .onSuccess {
                                val recipe = it.first()
                                val (metaModel, stepList) =
                                    recipe.toPresentation(recipeValidator, recipeTypes, recipeStepValidator)
                                recipeMetaModel = metaModel
                                recipeStepModelList = stepList
                                isEditing = true
                            }
                            .onFailure {
                                _eventError.value = Event(Unit)
                            }
                    } ?: run {
                        recipeMetaModel = RecipeEditorItem.RecipeMetaModel.create(
                            idGenerator, recipeTypes, recipeValidator
                        )
                        recipeStepModelList = listOf(
                            RecipeEditorItem.RecipeStepModel.create(
                                idGenerator, recipeStepValidator
                            )
                        )
                    }
                    notifyStepListChange()
                }
                .onFailure {
                    _eventError.value = Event(Unit)
                }
                _liveLoading.value = false
        }
        isInitialized = true
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
        _liveMoveToPosition.value = recipeStepModelList.size + 2
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
                val recipe = recipeMetaModel.toDomain(
                    recipeStepModelList,
                    liveRecipeTypes.value ?: emptyList()
                )
                saveRecipeUseCase(SaveLocalRecipeRequest(recipe))
                    .onSuccess {
                        if (isEditing) {
                            updateRemoteRecipeUseCase(UpdateRemoteRecipeRequest(recipe))
                                .onSuccess {
                                    _eventSaveResult.value = Event(true)
                                }
                                .onFailure {
                                    _eventSaveResult.value = Event(false)
                                }
                        } else {
                            _eventSaveResult.value = Event(true)
                        }
                    }.onFailure {
                        it.printStackTrace()
                        _eventSaveResult.value = Event(false)
                    }
            }
        }
    }
    
    private fun isRecipeValid(): Boolean {
        if (
            recipeMetaModel.liveTitleState.value != true ||
            recipeMetaModel.liveStuffState.value != true
        ) {
            _liveMoveToPosition.value = 0
            return false
        }
        
        recipeStepModelList.forEachIndexed { i, stepModel ->
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
