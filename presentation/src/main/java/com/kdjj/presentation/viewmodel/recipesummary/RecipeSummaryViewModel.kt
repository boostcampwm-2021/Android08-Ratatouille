package com.kdjj.presentation.viewmodel.recipesummary

import androidx.lifecycle.*
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.request.DeleteLocalRecipeRequest
import com.kdjj.domain.model.request.GetLocalRecipeFlowRequest
import com.kdjj.domain.model.request.UpdateLocalRecipeFavoriteRequest
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
    idGenerator: IdGenerator
) : ViewModel() {
    
    private val _liveRecipe = MutableLiveData<Recipe>()
    val liveRecipe: LiveData<Recipe> = _liveRecipe
    private val _eventNoInfo = MutableLiveData<Event<Unit>>()
    val eventNoInfo: LiveData<Event<Unit>> = _eventNoInfo
    private val _eventOpenRecipeDetail = MutableLiveData<Event<Unit>>()
    val eventOpenRecipeDetail: LiveData<Event<Unit>> = _eventOpenRecipeDetail
    private val _eventOpenRecipeEditor = MutableLiveData<Event<Unit>>()
    val eventOpenRecipeEditor: LiveData<Event<Unit>> = _eventOpenRecipeEditor
    private val _eventDeleteRecipeSuccess = MutableLiveData<Event<Unit>>()
    val eventDeleteRecipeSuccess: LiveData<Event<Unit>> = _eventDeleteRecipeSuccess
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
                        // TODO : Remote에서 레시피 가져오는 기능
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
            liveRecipe.value?.let { recipe ->
                deleteLocalRecipeUseCase(DeleteLocalRecipeRequest(recipe))
                    .onSuccess {
                        _eventDeleteRecipeSuccess.value = Event(Unit)
                    }
                // TODO : 실패 유저 피드백
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
