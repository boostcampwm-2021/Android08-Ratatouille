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
import com.kdjj.presentation.model.NEW_ID
import com.kdjj.presentation.model.RecipeEditorItem
import com.kdjj.presentation.model.toDomain
import com.kdjj.presentation.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Job
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
internal class RecipeEditorViewModel @Inject constructor(
    private val recipeValidator: RecipeValidator,
    private val recipeStepValidator: RecipeStepValidator,
    private val saveRecipeUseCase: ResultUseCase<SaveLocalRecipeRequest, Boolean>,
    private val fetchRecipeTypesUseCase: ResultUseCase<EmptyRequest, List<RecipeType>>,
    private val getLocalRecipeUseCase: ResultUseCase<GetLocalRecipeRequest, Recipe>,
    private val fetchRecipeTempUseCase: ResultUseCase<FetchRecipeTempRequest, Recipe?>,
    private val saveRecipeTempUseCase: ResultUseCase<SaveRecipeTempRequest, Unit>,
    private val deleteRecipeTempUseCase: ResultUseCase<DeleteRecipeTempRequest, Unit>,
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

    private val _liveEditing = MutableLiveData(false)
    val liveEditing: LiveData<Boolean> get() = _liveEditing

    private val _eventRecipeEditor = MutableLiveData<Event<RecipeEditorEvent>>()
    val eventRecipeEditor: LiveData<Event<RecipeEditorEvent>> get() = _eventRecipeEditor

    sealed class RecipeEditorEvent {
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

    private val compositeDisposable = CompositeDisposable()
    val editorSubject: PublishSubject<ButtonClick> = PublishSubject.create()

    private val tempSubject: PublishSubject<Unit> = PublishSubject.create()
    private var didEdit = false

    private val _liveTempLoading = MutableLiveData(false)
    val liveTempLoading: LiveData<Boolean> get() = _liveTempLoading

    init {
        editorSubject.throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                println(Thread.currentThread())
                when (it) {
                    ButtonClick.SAVE -> {
                        saveRecipe()
                    }
                    ButtonClick.ADD_STEP -> {
                        addRecipeStep()
                    }
                    else -> {}
                }
            }
            .also {
                compositeDisposable.add(it)
            }

        tempSubject.debounce(3, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                saveTempRecipe()
            }
            .also {
                compositeDisposable.add(it)
            }
    }

    fun doEdit() {
        didEdit = true
        tempSubject.onNext(Unit)
    }

    fun initializeWith(loadingRecipeId: String?) {
        if (isInitialized) return

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
                        tempRecipe = it
                        _eventRecipeEditor.value = Event(RecipeEditorEvent.TempDialog(recipeId))
                    } ?: if (recipeId == NEW_ID) {
                        createNewRecipe()
                    } else {
                        loadFromLocal(recipeId)
                    }
                }
                .onFailure {
                    _eventRecipeEditor.value = Event(RecipeEditorEvent.Error)
                }
            _liveLoading.value = false
        }
    }

    fun showRecipeFromTemp() {
        if (isInitialized) return
        val (metaModel, stepList) =
            tempRecipe.toPresentation(
                recipeValidator,
                _liveRecipeTypes.value ?: listOf(),
                recipeStepValidator
            )
        recipeMetaModel = metaModel
        _liveStepModelList.value = stepList
        isInitialized = true
    }

    fun showRecipeFromLocal(recipeId: String) {
        if (isInitialized) return
        if (recipeId == NEW_ID) {
            createNewRecipe()
        } else {
            viewModelScope.launch {
                loadFromLocal(recipeId)
            }
        }
        isInitialized = true
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
        isInitialized = true
    }

    private suspend fun loadFromLocal(recipeId: String) {
        getLocalRecipeUseCase(GetLocalRecipeRequest(recipeId))
            .onSuccess { recipe ->
                val (metaModel, stepList) =
                    recipe.toPresentation(recipeValidator, _liveRecipeTypes.value ?: listOf(), recipeStepValidator)
                recipeMetaModel = metaModel
                _liveStepModelList.value = stepList
                deleteRecipeTempUseCase(DeleteRecipeTempRequest(recipe.recipeId))
            }
            .onFailure {
                _eventRecipeEditor.value = Event(RecipeEditorEvent.Error)
            }
    }

    private fun saveTempRecipe() {
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
        _eventRecipeEditor.value = Event(RecipeEditorEvent.ExitDialog)
    }

    fun deleteTemp() {
        tempJob?.cancel()
        _liveTempLoading.value = false
        _liveLoading.value = true
        viewModelScope.launch {
            deleteRecipeTempUseCase(DeleteRecipeTempRequest(recipeMetaModel.recipeId))
            _liveLoading.value = false
            _eventRecipeEditor.value = Event(RecipeEditorEvent.Exit)
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
        _liveMoveToPosition.value = (_liveStepModelList.value?.size ?: 0) + 2
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
            _liveLoading.value = true
            viewModelScope.launch {
                val recipe = recipeMetaModel.toDomain(
                    _liveStepModelList.value ?: listOf(),
                    liveRecipeTypes.value ?: listOf()
                )
                if (_liveEditing.value == true) {
                    updateLocalRecipeUseCase(UpdateLocalRecipeRequest(recipe))
                } else {
                    saveRecipeUseCase(SaveLocalRecipeRequest(recipe))
                }.onSuccess {
                    if (recipe.state == RecipeState.UPLOAD) registerUploadTask(recipe.recipeId)
                    _eventRecipeEditor.value =
                        Event(RecipeEditorEvent.SaveResult(true))
                    deleteTemp()
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
            _liveMoveToPosition.value = 0
            return false
        }

        _liveStepModelList.value?.forEachIndexed { i, stepModel ->
            if (
                !recipeStepValidator.validateName(stepModel.liveName.value ?: "") ||
                !recipeStepValidator.validateDescription(stepModel.liveDescription.value ?: "") ||
                !recipeStepValidator.validateMinutes(stepModel.liveTimerMin.value ?: 0) ||
                !recipeStepValidator.validateSeconds(stepModel.liveTimerSec.value ?: 0)
            ) {
                _liveMoveToPosition.value = i + 1
                return false
            }
        }

        return true
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
