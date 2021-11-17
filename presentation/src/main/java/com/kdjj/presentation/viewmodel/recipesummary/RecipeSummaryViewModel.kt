package com.kdjj.presentation.viewmodel.recipesummary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.request.GetLocalRecipeFlowRequest
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeSummaryViewModel @Inject constructor(
    private val getRecipeFlowUseCase: UseCase<GetLocalRecipeFlowRequest, Flow<Recipe>>,
) : ViewModel() {
    
    private val _liveRecipe = MutableLiveData<Recipe>()
    val liveRecipe: LiveData<Recipe> = _liveRecipe
    private val _eventNoInfo = MutableLiveData<Event<Unit>>()
    val eventNoInfo: LiveData<Event<Unit>> = _eventNoInfo
    private var isInitialized = false
    
    fun initViewModel(recipeId: String?, recipeState: RecipeState?) {
        if (isInitialized) return
        if (recipeId == null || recipeState == null) notifyNoInfo()
        else {
            viewModelScope.launch {
                when (recipeState) {
                    RecipeState.CREATE, RecipeState.UPLOAD -> {
                        getRecipeFlowUseCase(GetLocalRecipeFlowRequest(recipeId))
                            .onSuccess { recipeFlow ->
                                recipeFlow.collect { recipe ->
                                    _liveRecipe.value = recipe
                                }
                            }
                    }
                    RecipeState.DOWNLOAD -> {
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
