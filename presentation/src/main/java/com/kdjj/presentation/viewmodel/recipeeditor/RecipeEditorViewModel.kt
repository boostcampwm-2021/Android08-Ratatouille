package com.kdjj.presentation.viewmodel.recipeeditor

import androidx.lifecycle.*
import androidx.work.*
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.RecipeStepType
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.model.request.*
import com.kdjj.domain.usecase.ResultUseCase
import com.kdjj.presentation.common.*
import com.kdjj.presentation.common.extensions.throttleFirst
import com.kdjj.presentation.model.NEW_ID
import com.kdjj.presentation.model.RecipeEditorItem
import com.kdjj.presentation.model.toDomain
import com.kdjj.presentation.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

@HiltViewModel
internal class RecipeEditorViewModel @Inject constructor(
    private val recipeValidator: RecipeValidator,
    private val recipeStepValidator: RecipeStepValidator,
    private val saveRecipeUseCase: ResultUseCase<SaveMyRecipeRequest, Unit>,
    private val fetchRecipeTypesUseCase: ResultUseCase<EmptyRequest, List<RecipeType>>,
    private val getMyRecipeUseCase: ResultUseCase<GetMyRecipeRequest, Recipe>,
    private val fetchRecipeTempUseCase: ResultUseCase<FetchRecipeTempRequest, Recipe?>,
    private val saveRecipeTempUseCase: ResultUseCase<SaveRecipeTempRequest, Unit>,
    private val deleteRecipeTempUseCase: ResultUseCase<DeleteRecipeTempRequest, Unit>,
    private val updateMyRecipeUseCase: ResultUseCase<UpdateMyRecipeRequest, Unit>,
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

    private val _liveEditing = MutableLiveData(false)
    val liveEditing: LiveData<Boolean> get() = _liveEditing

    private val _eventRecipeEditor = MutableLiveData<Event<RecipeEditorEvent>>()
    val eventRecipeEditor: LiveData<Event<RecipeEditorEvent>> get() = _eventRecipeEditor

    sealed class RecipeEditorEvent {
        class MoveToPosition(val idx: Int) : RecipeEditorEvent()
        class SaveResult(val isSuccess: Boolean) : RecipeEditorEvent()
        class TempDialog(val recipeId: String) : RecipeEditorEvent()
        object Error : RecipeEditorEvent()
        object ExitDialog : RecipeEditorEvent()
        object Exit : RecipeEditorEvent()
    }

    private lateinit var tempRecipe: Recipe
    private var tempJob: Job? = null

    enum class ButtonClick {
        SAVE,
        ADD_STEP
    }

    val clickFlow = MutableSharedFlow<ButtonClick>(extraBufferCapacity = 1)

    private val tempFlow = MutableSharedFlow<Unit>()
    private var oldRecipe: Recipe? = null

    private val _liveTempLoading = MutableLiveData(false)
    val liveTempLoading: LiveData<Boolean> get() = _liveTempLoading

    init {
        viewModelScope.launch {
            clickFlow.throttleFirst()
                .collect {
                    when (it) {
                        ButtonClick.SAVE -> {
                            saveRecipe()
                        }
                        ButtonClick.ADD_STEP -> {
                            addRecipeStep()
                        }
                    }
                }
        }

        viewModelScope.launch {
            tempFlow.debounce(3000)
                .collect {
                    saveTempRecipe()
                }
        }
    }

    fun doEdit() {
        viewModelScope.launch {
            tempFlow.emit(Unit)
        }
    }

    private fun isSameWithOld(): Boolean {
        oldRecipe?.let { oldRecipe ->
            if (
                recipeMetaModel.liveTitle.value != oldRecipe.title ||
                recipeMetaModel.liveStuff.value != oldRecipe.stuff ||
                recipeMetaModel.liveRecipeImgPath.value != oldRecipe.imgPath ||
                recipeMetaModel.liveRecipeTypeInt.value != liveRecipeTypes.value?.indexOf(oldRecipe.type)
            ) {
                return false
            }

            if (_liveStepModelList.value?.size != oldRecipe.stepList.size) {
                return false
            }

            _liveStepModelList.value?.let { stepList ->
                stepList.forEachIndexed { idx, model ->
                    if (
                        model.liveName.value != oldRecipe.stepList[idx].name ||
                        model.liveDescription.value != oldRecipe.stepList[idx].description ||
                        model.liveImgPath.value != oldRecipe.stepList[idx].imgPath ||
                        model.liveTimerMin.value != oldRecipe.stepList[idx].seconds / 60 ||
                        model.liveTimerSec.value != oldRecipe.stepList[idx].seconds % 60 ||
                        model.liveTypeInt.value != oldRecipe.stepList[idx].type.ordinal
                    ) {
                        return false
                    }
                }
            }
            return true
        } ?: run {
            if (
                recipeMetaModel.liveTitle.value?.isEmpty() != true ||
                recipeMetaModel.liveStuff.value?.isEmpty() != true ||
                recipeMetaModel.liveRecipeImgPath.value?.isEmpty() != true ||
                recipeMetaModel.liveRecipeTypeInt.value != 0
            ) {
                return false
            }

            _liveStepModelList.value?.get(0)?.let { model ->
                if (
                    model.liveName.value?.isEmpty() != true ||
                    model.liveDescription.value?.isEmpty() != true ||
                    model.liveImgPath.value?.isEmpty() != true ||
                    model.liveTimerMin.value != 0 ||
                    model.liveTimerSec.value != 0 ||
                    model.liveTypeInt.value != 0
                ) {
                    return false
                }
            }

            return true
        }
    }

    fun initializeWith(loadingRecipeId: String?) {
        if (_liveStepModelList.value != null) return

        _liveLoading.value = true
        val recipeId = loadingRecipeId?.also {
            if (loadingRecipeId.isNotEmpty()) {
                _liveEditing.value = true
            }
        } ?: NEW_ID

        viewModelScope.launch {
            fetchRecipeTypesUseCase(EmptyRequest)
                .onSuccess { recipeTypes ->
                    _liveRecipeTypes.value = recipeTypes
                    fetchRecipeTempUseCase(FetchRecipeTempRequest(recipeId)).getOrNull()?.let {
                        _liveLoading.value = false
                        tempRecipe = it
                        _eventRecipeEditor.value = Event(RecipeEditorEvent.TempDialog(recipeId))
                    } ?: if (recipeId == NEW_ID) {
                        createNewRecipe()
                    } else {
                        loadFromLocal(recipeId)
                    }
                }
                .onFailure {
                    _liveLoading.value = false
                    _eventRecipeEditor.value = Event(RecipeEditorEvent.Error)
                }
        }
    }

    fun showRecipeFromTemp() {
        if (_liveStepModelList.value != null) return

        _liveLoading.value = true
        val (metaModel, stepList) =
            tempRecipe.toPresentation(
                recipeValidator,
                _liveRecipeTypes.value ?: listOf(),
                recipeStepValidator
            )
        recipeMetaModel = metaModel
        _liveStepModelList.value = stepList

        if (tempRecipe.recipeId.isNotEmpty()) {
            viewModelScope.launch {
                oldRecipe = getMyRecipeUseCase(GetMyRecipeRequest(tempRecipe.recipeId)).getOrNull()
                _liveLoading.value = false
            }
        } else {
            _liveLoading.value = false
        }
    }

    fun showRecipeFromLocal(recipeId: String) {
        if (_liveStepModelList.value != null) return
        if (recipeId == NEW_ID) {
            createNewRecipe()
        } else {
            viewModelScope.launch {
                loadFromLocal(recipeId)
            }
        }
    }

    private fun createNewRecipe() {
        recipeMetaModel = RecipeEditorItem.RecipeMetaModel.create(
            idGenerator, recipeValidator
        )
        _liveStepModelList.value = listOf(
            RecipeEditorItem.RecipeStepModel.create(
                recipeStepValidator
            )
        )
        viewModelScope.launch {
            deleteRecipeTempUseCase(DeleteRecipeTempRequest(NEW_ID))
        }
        _liveLoading.value = false
    }

    private suspend fun loadFromLocal(recipeId: String) {
        getMyRecipeUseCase(GetMyRecipeRequest(recipeId))
            .onSuccess { recipe ->
                oldRecipe = recipe
                val (metaModel, stepList) =
                    recipe.toPresentation(recipeValidator, _liveRecipeTypes.value ?: listOf(), recipeStepValidator)
                recipeMetaModel = metaModel
                _liveStepModelList.value = stepList
                deleteRecipeTempUseCase(DeleteRecipeTempRequest(recipe.recipeId))
            }
            .onFailure {
                _eventRecipeEditor.value = Event(RecipeEditorEvent.Error)
            }
        _liveLoading.value = false

    }

    private fun saveTempRecipe() {
        if (
            _liveStepModelList.value == null || isSameWithOld() ||
            (_eventRecipeEditor.value?.peekContent() as? RecipeEditorEvent.SaveResult)?.isSuccess == true
        ) {
            return
        }

        _liveTempLoading.value = true
        tempJob?.cancel()
        tempJob = viewModelScope.launch {
            val recipe = recipeMetaModel.toDomain(
                _liveStepModelList.value ?: listOf(),
                liveRecipeTypes.value ?: listOf()
            )
            saveRecipeTempUseCase(SaveRecipeTempRequest(recipe))
            _liveTempLoading.value = false
        }
    }

    fun showExitDialog() {
        if (isSameWithOld()) {
            deleteTemp(true)
        } else {
            _eventRecipeEditor.value = Event(RecipeEditorEvent.ExitDialog)
        }
    }

    fun deleteTemp(finish: Boolean) {
        tempJob?.cancel()
        _liveTempLoading.value = false
        _liveLoading.value = true
        viewModelScope.launch {
            deleteRecipeTempUseCase(DeleteRecipeTempRequest(recipeMetaModel.recipeId))
            _liveLoading.value = false
            if (finish) {
                _eventRecipeEditor.value = Event(RecipeEditorEvent.Exit)
            }
        }
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
            doEdit()
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

    private fun addRecipeStep() {
        _liveStepModelList.value = (_liveStepModelList.value ?: listOf()) +
                RecipeEditorItem.RecipeStepModel.create(recipeStepValidator)
        _eventRecipeEditor.value = Event(
            RecipeEditorEvent.MoveToPosition((_liveStepModelList.value?.size ?: 0) + 2)
        )
        doEdit()
    }

    fun removeRecipeStep(position: Int) {
        _liveStepModelList.value?.let { modelList ->
            _liveStepModelList.value = modelList.subList(0, position - 1) +
                    modelList.subList(position, modelList.size)
            doEdit()
        }
    }

    fun changeRecipeStepPosition(from: Int, to: Int) {
        _liveStepModelList.value?.let { modelList ->
            _liveStepModelList.value = modelList.toMutableList().apply {
                set(from - 1, set(to - 1, get(from - 1)))
            }
            doEdit()
        }
    }

    private fun saveRecipe() {
        _liveRegisterHasPressed.value = true
        if (isRecipeValid()) {
            if (isSameWithOld()) {
                _eventRecipeEditor.value = Event(RecipeEditorEvent.SaveResult(true))
                return
            }
            _liveLoading.value = true
            viewModelScope.launch {
                val recipe = recipeMetaModel.toDomain(
                    _liveStepModelList.value ?: listOf(),
                    liveRecipeTypes.value ?: listOf()
                )
                if (_liveEditing.value == true) {
                    updateMyRecipeUseCase(UpdateMyRecipeRequest(recipe))
                } else {
                    saveRecipeUseCase(SaveMyRecipeRequest(recipe))
                }.onSuccess {
                    if (recipe.state == RecipeState.UPLOAD) registerUploadTask(recipe.recipeId)
                    _eventRecipeEditor.value =
                        Event(RecipeEditorEvent.SaveResult(true))
                    deleteTemp(false)
                }.onFailure {
                    _eventRecipeEditor.value =
                        Event(RecipeEditorEvent.SaveResult(false))
                }
                _liveLoading.value = false
            }
        }
    }

    private fun registerUploadTask(updatedRecipeId: String) {
        workManager.cancelAllWorkByTag(updatedRecipeId)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val updateWorkerRequest = OneTimeWorkRequestBuilder<RecipeUploadWorker>().apply {
            setInputData(workDataOf(UPDATED_RECIPE_ID to updatedRecipeId))
        }
            .setConstraints(constraints)
            .addTag(updatedRecipeId)
            .build()
        workManager.enqueue(updateWorkerRequest)
    }

    private fun isRecipeValid(): Boolean {
        if (
            !recipeValidator.validateTitle(recipeMetaModel.liveTitle.value ?: "") ||
            !recipeValidator.validateStuff(recipeMetaModel.liveStuff.value ?: "")
        ) {
            _eventRecipeEditor.value = Event(RecipeEditorEvent.MoveToPosition(0))
            return false
        }

        _liveStepModelList.value?.forEachIndexed { i, stepModel ->
            if (
                !recipeStepValidator.validateName(stepModel.liveName.value ?: "") ||
                !recipeStepValidator.validateDescription(stepModel.liveDescription.value ?: "") ||
                !recipeStepValidator.validateMinutes(stepModel.liveTimerMin.value ?: 0) ||
                !recipeStepValidator.validateSeconds(stepModel.liveTimerSec.value ?: 0)
            ) {
                _eventRecipeEditor.value = Event(RecipeEditorEvent.MoveToPosition(i + 1))
                return false
            }
        }

        return true
    }
}
