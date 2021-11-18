package com.kdjj.presentation.viewmodel.recipesummary

import androidx.lifecycle.*
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.request.GetLocalRecipeFlowRequest
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.common.IdGenerator
import com.kdjj.presentation.model.RecipeSummaryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeSummaryViewModel @Inject constructor(
    private val getRecipeFlowUseCase: UseCase<GetLocalRecipeFlowRequest, Flow<Recipe>>,
    idGenerator: IdGenerator
) : ViewModel() {
    
    private val _liveRecipe = MutableLiveData<Recipe>()
    val liveRecipe: LiveData<Recipe> = _liveRecipe
    private val _eventNoInfo = MutableLiveData<Event<Unit>>()
    val eventNoInfo: LiveData<Event<Unit>> = _eventNoInfo
    val liveRecipeSummaryType: LiveData<RecipeSummaryType> = _liveRecipe.switchMap { recipe ->
        MutableLiveData(
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
    }
    private var isInitialized = false
    private val userId = idGenerator.getDeviceId()
    
    fun initViewModel(recipeId: String?, recipeState: RecipeState?) {
        if (isInitialized) return
        if (recipeId == null || recipeState == null) notifyNoInfo()
        else {
            viewModelScope.launch {
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
    
    private fun notifyNoInfo() {
        _eventNoInfo.value = Event(Unit)
    }
}
