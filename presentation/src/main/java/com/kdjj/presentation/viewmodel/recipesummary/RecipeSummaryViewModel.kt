package com.kdjj.presentation.viewmodel.recipesummary

import androidx.lifecycle.*
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
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
    private val idGenerator: IdGenerator
) : ViewModel() {

    private val _liveRecipe = MutableLiveData<Recipe>()
    val liveRecipe: LiveData<Recipe> = _liveRecipe

    private val _eventLoadError = MutableLiveData<Event<Unit>>()
    val eventLoadError: LiveData<Event<Unit>> = _eventLoadError

    private val _eventOpenRecipeDetail = MutableLiveData<Event<Recipe>>()
    val eventOpenRecipeDetail: LiveData<Event<Recipe>> = _eventOpenRecipeDetail

    private val _eventOpenRecipeEditor = MutableLiveData<Event<Recipe>>()
    val eventOpenRecipeEditor: LiveData<Event<Recipe>> = _eventOpenRecipeEditor

    private val _eventDeleteFinish = MutableLiveData<Event<Boolean>>()
    val eventDeleteFinish: LiveData<Event<Boolean>> = _eventDeleteFinish

    private val _eventUploadFinish = MutableLiveData<Event<Boolean>>()
    val eventUploadFinish: LiveData<Event<Boolean>> = _eventUploadFinish

    private val _eventSaveFinish = MutableLiveData<Event<Boolean>>()
    val eventSaveFinish: LiveData<Event<Boolean>> = _eventSaveFinish

    private val _eventUpdateFavoriteFinish = MutableLiveData<Event<Boolean>>()
    val eventUpdateFavoriteFinish: LiveData<Event<Boolean>> = _eventUpdateFavoriteFinish

    private val _liveLoading = MutableLiveData(false)
    val liveLoading: LiveData<Boolean> get() = _liveLoading

    val eventInitView: LiveData<Event<RecipeSummaryType>> =
        _liveRecipe.switchMap { recipe ->
            MutableLiveData(
                Event(
                    when {
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
                            _eventLoadError.value = Event(Unit)
                            RecipeSummaryType.MY_SAVE_RECIPE
                        }
                    }
                )
            )
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
                when (recipeState) {
                    RecipeState.CREATE,
                    RecipeState.UPLOAD,
                    RecipeState.DOWNLOAD -> {
                        getLocalRecipeFlowUseCase(GetLocalRecipeFlowRequest(recipeId))
                                .collect { recipe ->
                                    _liveRecipe.value = recipe
                                }
                    }
                    RecipeState.NETWORK -> {
                        fetchRemoteRecipeUseCase(FetchRemoteRecipeRequest(recipeId))
                                .onSuccess { recipe ->
                                    _liveRecipe.value = recipe
                                    increaseViewCountUseCase(IncreaseRemoteRecipeViewCountRequest(recipe))
                                }
                    }
                }
            }
            _liveLoading.value = false
        }

        isInitialized = true
    }

    fun updateRecipeFavorite() {
        _liveLoading.value = true
        viewModelScope.launch {
            liveRecipe.value?.let { recipe ->
                val favoriteResult =
                    updateLocalRecipeFavoriteUseCase(UpdateLocalRecipeFavoriteRequest(recipe))
                _eventUpdateFavoriteFinish.value = Event(favoriteResult.isSuccess)
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
                _eventDeleteFinish.value = Event(deleteResult.isSuccess)
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

                _eventSaveFinish.value = Event(saveResult.isSuccess)
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

                _eventSaveFinish.value = Event(saveResult.isSuccess)
            }
            _liveLoading.value = false
        }
    }

    fun uploadRecipe() {
        _liveLoading.value = true
        viewModelScope.launch {
            liveRecipe.value?.let { recipe ->
                val uploadResult = uploadRecipeUseCase(UploadRecipeRequest(recipe))
                _eventUploadFinish.value = Event(uploadResult.isSuccess)
            }
            _liveLoading.value = false
        }
    }

    fun openRecipeDetail() {
        liveRecipe.value?.let { recipe ->
            _eventOpenRecipeDetail.value = Event(recipe)
        }
    }

    fun openRecipeEditor() {
        liveRecipe.value?.let { recipe ->
            _eventOpenRecipeEditor.value = Event(recipe)
        }
    }

    private fun notifyNoInfo() {
        _eventLoadError.value = Event(Unit)
    }
}
