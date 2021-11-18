package com.kdjj.presentation.viewmodel.recipesummary

import androidx.lifecycle.*
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.request.*
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.common.IdGenerator
import com.kdjj.presentation.model.RecipeSummaryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeSummaryViewModel @Inject constructor(
    private val getRecipeFlowUseCase: UseCase<GetLocalRecipeFlowRequest, Flow<Recipe>>,
    private val updateLocalRecipeFavoriteUseCase: UseCase<UpdateLocalRecipeFavoriteRequest, Boolean>,
    private val deleteLocalRecipeUseCase: UseCase<DeleteLocalRecipeRequest, Boolean>,
    private val deleteRemoteRecipeUseCase: UseCase<DeleteRemoteRecipeRequest, Unit>,
    private val fetchRemoteRecipeUseCase: UseCase<FetchRemoteRecipeRequest, Recipe>,
    private val saveLocalRecipeUseCase: UseCase<SaveLocalRecipeRequest, Boolean>,
    private val uploadRecipeUseCase: UseCase<UploadRecipeRequest, Unit>,
    private val idGenerator: IdGenerator
) : ViewModel() {
    
    private val _liveRecipe = MutableLiveData<Recipe>()
    val liveRecipe: LiveData<Recipe> = _liveRecipe
    private val _eventNoInfo = MutableLiveData<Event<Unit>>()
    val eventNoInfo: LiveData<Event<Unit>> = _eventNoInfo
    private val _eventOpenRecipeDetail = MutableLiveData<Event<Unit>>()
    val eventOpenRecipeDetail: LiveData<Event<Unit>> = _eventOpenRecipeDetail
    private val _eventOpenRecipeEditor = MutableLiveData<Event<Unit>>()
    val eventOpenRecipeEditor: LiveData<Event<Unit>> = _eventOpenRecipeEditor
    private val _eventDeleteFinish = MutableLiveData<Event<Boolean>>()
    val eventDeleteFinish: LiveData<Event<Boolean>> = _eventDeleteFinish
    private val _eventUploadFinish = MutableLiveData<Event<Boolean>>()
    val eventUploadFinish: LiveData<Event<Boolean>> = _eventUploadFinish
    private val _eventSaveFinish = MutableLiveData<Event<Boolean>>()
    val eventSaveFinish: LiveData<Event<Boolean>> = _eventSaveFinish
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
                        else -> throw Exception("Unknown RecipeSummaryType")
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
            collectJob = viewModelScope.launch {
                when (recipeState) {
                    RecipeState.CREATE,
                    RecipeState.UPLOAD,
                    RecipeState.DOWNLOAD -> {
                        getRecipeFlowUseCase(GetLocalRecipeFlowRequest(recipeId))
                            .onSuccess { recipeFlow ->
                                recipeFlow.collect { recipe ->
                                    _liveRecipe.value = recipe
                                }
                            }
                    }
                    RecipeState.NETWORK -> {
                        fetchRemoteRecipeUseCase(FetchRemoteRecipeRequest(recipeId))
                            .onSuccess { recipe ->
                                _liveRecipe.value = recipe
                            }
                    }
                }
            }
        }
        
        isInitialized = true
    }
    
    fun updateRecipeFavorite() =
        viewModelScope.launch {
            liveRecipe.value?.let { recipe ->
                updateLocalRecipeFavoriteUseCase(UpdateLocalRecipeFavoriteRequest(recipe))
                // TODO : 성공 실패 유저 피드백
            }
        }
    
    fun deleteRecipe() =
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
        }
    
    fun saveRecipeToLocal() =
        viewModelScope.launch {
            liveRecipe.value?.let { recipe ->
                val newRecipeId = idGenerator.generateId()
                val saveResult = saveLocalRecipeUseCase(
                    SaveLocalRecipeRequest(
                        recipe.copy(
                            recipeId = newRecipeId,
                            state = RecipeState.DOWNLOAD
                        )
                    )
                )
                _eventSaveFinish.value = Event(saveResult.isSuccess)
            }
        }
    
    fun saveRecipeToLocalWithFavorite() =
        viewModelScope.launch {
            liveRecipe.value?.let { recipe ->
                val newRecipeId = idGenerator.generateId()
                saveLocalRecipeUseCase(
                    SaveLocalRecipeRequest(
                        recipe.copy(
                            recipeId = newRecipeId,
                            state = RecipeState.DOWNLOAD
                        )
                    )
                ).onSuccess {
                    updateLocalRecipeFavoriteUseCase(
                        UpdateLocalRecipeFavoriteRequest(
                            recipe.copy(
                                recipeId = newRecipeId,
                                state = RecipeState.DOWNLOAD
                            )
                        )
                    )
                    // TODO : 성공 실패 피드백
                }
            }
        }
    
    fun uploadRecipe() =
        viewModelScope.launch {
            liveRecipe.value?.let { recipe ->
                val uploadResult = uploadRecipeUseCase(UploadRecipeRequest(recipe))
                _eventUploadFinish.value = Event(uploadResult.isSuccess)
            }
        }
    
    fun openRecipeDetail() {
        _eventOpenRecipeDetail.value = Event(Unit)
    }
    
    fun openRecipeEditor() {
        _eventOpenRecipeEditor.value = Event(Unit)
    }
    
    private fun notifyNoInfo() {
        _eventNoInfo.value = Event(Unit)
    }
}
