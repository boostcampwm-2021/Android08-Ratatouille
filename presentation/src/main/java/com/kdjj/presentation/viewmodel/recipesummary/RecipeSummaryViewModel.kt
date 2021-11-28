package com.kdjj.presentation.viewmodel.recipesummary

import androidx.lifecycle.*
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.model.request.*
import com.kdjj.domain.usecase.FlowUseCase
import com.kdjj.domain.usecase.ResultUseCase
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.common.IdGenerator
import com.kdjj.presentation.model.RecipeSummaryType
import com.kdjj.presentation.model.UpdateFavoriteResult
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class RecipeSummaryViewModel @Inject constructor(
    private val getMyRecipeFlowUseCase: FlowUseCase<GetMyRecipeRequest, Recipe>,
    private val updateMyRecipeFavoriteUseCase: ResultUseCase<UpdateMyRecipeFavoriteRequest, Boolean>,
    private val deleteMyRecipeUseCase: ResultUseCase<DeleteMyRecipeRequest, Boolean>,
    private val deleteUploadedRecipeUseCase: ResultUseCase<DeleteUploadedRecipeRequest, Unit>,
    private val fetchOthersRecipeUseCase: ResultUseCase<FetchOthersRecipeRequest, Recipe>,
    private val saveMyRecipeUseCase: ResultUseCase<SaveMyRecipeRequest, Boolean>,
    private val uploadRecipeUseCase: ResultUseCase<UploadRecipeRequest, Unit>,
    private val increaseViewCountUseCase: ResultUseCase<IncreaseOthersRecipeViewCountRequest, Unit>,
    private val fetchRecipeTypeListUseCase: ResultUseCase<EmptyRequest, List<RecipeType>>,
    private val idGenerator: IdGenerator
) : ViewModel() {

    private val _liveRecipe = MutableLiveData<Recipe>()
    val liveRecipe: LiveData<Recipe> = _liveRecipe

    private val _liveLoading = MutableLiveData(false)
    val liveLoading: LiveData<Boolean> get() = _liveLoading

    private val _liveFabState = MutableLiveData<Pair<RecipeSummaryType, Boolean>>()
    val liveFabState: LiveData<Pair<RecipeSummaryType, Boolean>> get() = _liveFabState

    private val _eventRecipeSummary = MutableLiveData<Event<RecipeSummaryEvent>>()
    val eventRecipeSummary: LiveData<Event<RecipeSummaryEvent>> = _eventRecipeSummary

    sealed class RecipeSummaryEvent {
        object LoadError : RecipeSummaryEvent()
        class DeleteFinish(val flag: Boolean) : RecipeSummaryEvent()
        object DeleteConfirm : RecipeSummaryEvent()
        class UploadFinish(val flag: Boolean) : RecipeSummaryEvent()
        class SaveFinish(val flag: Boolean) : RecipeSummaryEvent()
        class UpdateFavoriteFinish(val result: UpdateFavoriteResult) : RecipeSummaryEvent()
    }

    sealed class ButtonClick {
        class OpenRecipeDetail(val item: Recipe) : ButtonClick()
        class OpenRecipeEditor(val item: Recipe) : ButtonClick()
    }

    enum class FabClick {
        UpdateRecipeFavorite,
        RequestDeleteConfirm,
        SaveRecipeToLocal,
        SaveRecipeToLocalWithFavorite,
        UploadRecipe
    }

    private var isInitialized = false
    private val userId = idGenerator.getDeviceId()
    private var collectJob: Job? = null

    private val compositeDisposable = CompositeDisposable()
    val fabClickSubject: PublishSubject<FabClick> = PublishSubject.create()
    val summarySubject: PublishSubject<ButtonClick> = PublishSubject.create()

    init {
        fabClickSubject.throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                when (it) {
                    FabClick.UpdateRecipeFavorite -> {
                        updateRecipeFavorite()
                    }
                    FabClick.RequestDeleteConfirm -> {
                        requestDeleteConfirm()
                    }
                    FabClick.SaveRecipeToLocal -> {
                        saveRecipeToLocal()
                    }
                    FabClick.SaveRecipeToLocalWithFavorite -> {
                        saveRecipeToLocalWithFavorite()
                    }
                    FabClick.UploadRecipe -> {
                        uploadRecipe()
                    }
                    else -> {}
                }
            }.also {
                compositeDisposable.add(it)
            }
    }

    fun initViewModel(recipeId: String?, recipeState: RecipeState?) {
        if (isInitialized) return
        if (recipeId == null || recipeState == null) notifyNoInfo()
        else {
            _liveLoading.value = true
            collectJob = viewModelScope.launch {
                fetchRecipeTypeListUseCase(EmptyRequest)
                when (recipeState) {
                    RecipeState.CREATE,
                    RecipeState.UPLOAD,
                    RecipeState.DOWNLOAD -> {
                        getMyRecipeFlowUseCase(GetMyRecipeRequest(recipeId))
                            .collect { recipe ->
                                _liveRecipe.value = recipe
                                updateFabState(recipe)
                                _liveLoading.value = false
                            }
                    }
                    RecipeState.NETWORK -> {
                        fetchOthersRecipeUseCase(FetchOthersRecipeRequest(recipeId))
                            .onSuccess { recipe ->
                                _liveRecipe.value = recipe
                                updateFabState(recipe)
                                increaseViewCountUseCase(
                                    IncreaseOthersRecipeViewCountRequest(
                                        recipe
                                    )
                                )
                                _liveLoading.value = false
                            }
                    }
                }
            }
        }

        isInitialized = true
    }

    private fun updateFabState(recipe: Recipe) {
        val oldRecipeSummaryType = _liveFabState.value?.first
        val newRecipeSummaryType = when {
            recipe.authorId == userId && (recipe.state == RecipeState.CREATE || recipe.state == RecipeState.UPLOAD) -> {
                RecipeSummaryType.MY_SAVE_RECIPE
            }
            recipe.state == RecipeState.DOWNLOAD -> {
                RecipeSummaryType.MY_SAVE_OTHER_RECIPE
            }
            recipe.authorId == userId && recipe.state == RecipeState.NETWORK -> {
                RecipeSummaryType.MY_SERVER_RECIPE
            }
            recipe.authorId != userId && recipe.state == RecipeState.NETWORK -> {
                RecipeSummaryType.OTHER_SERVER_RECIPE
            }
            else -> {
                _eventRecipeSummary.value = Event(RecipeSummaryEvent.LoadError)
                RecipeSummaryType.MY_SAVE_RECIPE
            }
        }

        if (oldRecipeSummaryType != newRecipeSummaryType) {
            _liveFabState.value = Pair(newRecipeSummaryType, false)
        }
    }

    private fun updateRecipeFavorite() {
        _liveLoading.value = true
        viewModelScope.launch {
            liveRecipe.value?.let { recipe ->
                val favoriteResult =
                    updateMyRecipeFavoriteUseCase(UpdateMyRecipeFavoriteRequest(recipe))
                        .onSuccess { newFavorite ->
                            val favoriteState = if (newFavorite) {
                                UpdateFavoriteResult.ADD
                            } else {
                                UpdateFavoriteResult.REMOVE
                            }
                            _eventRecipeSummary.value =
                                Event(RecipeSummaryEvent.UpdateFavoriteFinish(favoriteState))
                        }.onFailure {
                            _eventRecipeSummary.value =
                                Event(RecipeSummaryEvent.UpdateFavoriteFinish(UpdateFavoriteResult.ERROR))
                        }

            }
            _liveLoading.value = false
        }
    }

    private fun requestDeleteConfirm() {
        _eventRecipeSummary.value = Event(RecipeSummaryEvent.DeleteConfirm)
    }

    fun deleteRecipe() {
        _liveLoading.value = true
        viewModelScope.launch {
            collectJob?.cancel()
            val recipeState = liveRecipe.value?.state ?: return@launch

            liveRecipe.value?.let { recipe ->
                val deleteResult = when (recipeState) {
                    RecipeState.CREATE,
                    RecipeState.UPLOAD,
                    RecipeState.DOWNLOAD -> {
                        deleteMyRecipeUseCase(DeleteMyRecipeRequest(recipe))
                    }
                    RecipeState.NETWORK -> {
                        deleteUploadedRecipeUseCase(DeleteUploadedRecipeRequest(recipe))
                    }
                }
                _eventRecipeSummary.value =
                    Event(RecipeSummaryEvent.DeleteFinish(deleteResult.isSuccess))
            }
            _liveLoading.value = false
        }
    }

    private fun saveRecipeToLocal() {
        _liveLoading.value = true
        viewModelScope.launch {
            liveRecipe.value?.let { recipe ->
                val newRecipeId = idGenerator.generateId()
                val newRecipe = recipe.copy(
                    recipeId = newRecipeId,
                    state = RecipeState.DOWNLOAD,
                    stepList = recipe.stepList.map { it.copy(stepId = idGenerator.generateId()) }
                )
                val saveResult = saveMyRecipeUseCase(SaveMyRecipeRequest(newRecipe))

                _eventRecipeSummary.value =
                    Event(RecipeSummaryEvent.SaveFinish(saveResult.isSuccess))
            }
            _liveLoading.value = false
        }
    }

    private fun saveRecipeToLocalWithFavorite() {
        _liveLoading.value = true
        viewModelScope.launch {
            liveRecipe.value?.let { recipe ->
                val newRecipeId = idGenerator.generateId()
                val newRecipe = recipe.copy(
                    recipeId = newRecipeId,
                    state = RecipeState.DOWNLOAD,
                    isFavorite = true,
                    stepList = recipe.stepList.map { it.copy(stepId = idGenerator.generateId()) }
                )
                val saveResult = saveMyRecipeUseCase(SaveMyRecipeRequest(newRecipe))

                _eventRecipeSummary.value =
                    Event(RecipeSummaryEvent.SaveFinish(saveResult.isSuccess))
            }
            _liveLoading.value = false
        }
    }

    private fun uploadRecipe() {
        _liveLoading.value = true
        viewModelScope.launch {
            liveRecipe.value?.let { recipe ->
                val uploadResult = uploadRecipeUseCase(UploadRecipeRequest(recipe))
                _eventRecipeSummary.value =
                    Event(RecipeSummaryEvent.UploadFinish(uploadResult.isSuccess))
            }
            _liveLoading.value = false
        }
    }

    fun openRecipeDetail() {
        liveRecipe.value?.let { recipe ->
            summarySubject.onNext(ButtonClick.OpenRecipeDetail(recipe))
        }
    }

    fun openRecipeEditor() {
        liveRecipe.value?.let { recipe ->
            summarySubject.onNext(ButtonClick.OpenRecipeEditor(recipe))
        }
    }

    fun changeFabState() {
        _liveFabState.value?.let { (recipeSummaryType, isFabOpen) ->
            _liveFabState.value = Pair(recipeSummaryType, !isFabOpen)
        }
    }

    private fun notifyNoInfo() {
        _eventRecipeSummary.value = Event(RecipeSummaryEvent.LoadError)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
