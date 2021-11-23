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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeSummaryViewModel @Inject constructor(
    private val getLocalRecipeFlowUseCase: FlowUseCase<GetLocalRecipeFlowRequest, Recipe>,
    private val updateLocalRecipeFavoriteUseCase: ResultUseCase<UpdateLocalRecipeFavoriteRequest, Boolean>,
    private val deleteLocalRecipeUseCase: ResultUseCase<DeleteLocalRecipeRequest, Boolean>,
    private val deleteRemoteRecipeUseCase: ResultUseCase<DeleteRemoteRecipeRequest, Unit>,
    private val fetchRemoteRecipeUseCase: ResultUseCase<FetchRemoteRecipeRequest, Recipe>,
    private val saveLocalRecipeUseCase: ResultUseCase<SaveLocalRecipeRequest, Boolean>,
    private val uploadRecipeUseCase: ResultUseCase<UploadRecipeRequest, Unit>,
    private val increaseViewCountUseCase: ResultUseCase<IncreaseRemoteRecipeViewCountRequest, Unit>,
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
        class OpenRecipeDetail(val item: Recipe) : RecipeSummaryEvent()
        class OpenRecipeEditor(val item: Recipe) : RecipeSummaryEvent()
        class DeleteFinish(val flag: Boolean) : RecipeSummaryEvent()
        class UploadFinish(val flag: Boolean) : RecipeSummaryEvent()
        class SaveFinish(val flag: Boolean) : RecipeSummaryEvent()
        class UpdateFavoriteFinish(val flag: Boolean) : RecipeSummaryEvent()
    }

    private var isInitialized = false
    private val userId = idGenerator.getDeviceId()
    private var collectJob: Job? = null

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
                        getLocalRecipeFlowUseCase(GetLocalRecipeFlowRequest(recipeId))
                            .collect { recipe ->
                                _liveRecipe.value = recipe
                                updateFabState(recipe)
                                _liveLoading.value = false
                            }
                    }
                    RecipeState.NETWORK -> {
                        fetchRemoteRecipeUseCase(FetchRemoteRecipeRequest(recipeId))
                            .onSuccess { recipe ->
                                _liveRecipe.value = recipe
                                updateFabState(recipe)
                                increaseViewCountUseCase(
                                    IncreaseRemoteRecipeViewCountRequest(
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

    fun updateRecipeFavorite() {
        _liveLoading.value = true
        viewModelScope.launch {
            liveRecipe.value?.let { recipe ->
                val favoriteResult =
                    updateLocalRecipeFavoriteUseCase(UpdateLocalRecipeFavoriteRequest(recipe))
                _eventRecipeSummary.value =
                    Event(RecipeSummaryEvent.UpdateFavoriteFinish(favoriteResult.isSuccess))
            }
            _liveLoading.value = false
        }
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
                        deleteLocalRecipeUseCase(DeleteLocalRecipeRequest(recipe))
                    }
                    RecipeState.NETWORK -> {
                        deleteRemoteRecipeUseCase(DeleteRemoteRecipeRequest(recipe))
                    }
                }
                _eventRecipeSummary.value =
                    Event(RecipeSummaryEvent.DeleteFinish(deleteResult.isSuccess))
            }
            _liveLoading.value = false
        }
    }

    fun saveRecipeToLocal() {
        _liveLoading.value = true
        viewModelScope.launch {
            liveRecipe.value?.let { recipe ->
                val newRecipeId = idGenerator.generateId()
                val newRecipe = recipe.copy(
                    recipeId = newRecipeId,
                    state = RecipeState.DOWNLOAD,
                    stepList = recipe.stepList.map { it.copy(stepId = idGenerator.generateId()) }
                )
                val saveResult = saveLocalRecipeUseCase(SaveLocalRecipeRequest(newRecipe))

                _eventRecipeSummary.value =
                    Event(RecipeSummaryEvent.SaveFinish(saveResult.isSuccess))
            }
            _liveLoading.value = false
        }
    }

    fun saveRecipeToLocalWithFavorite() {
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
                val saveResult = saveLocalRecipeUseCase(SaveLocalRecipeRequest(newRecipe))

                _eventRecipeSummary.value =
                    Event(RecipeSummaryEvent.SaveFinish(saveResult.isSuccess))
            }
            _liveLoading.value = false
        }
    }

    fun uploadRecipe() {
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
            _eventRecipeSummary.value = Event(RecipeSummaryEvent.OpenRecipeDetail(recipe))
        }
    }

    fun openRecipeEditor() {
        liveRecipe.value?.let { recipe ->
            _eventRecipeSummary.value = Event(RecipeSummaryEvent.OpenRecipeEditor(recipe))
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
}
