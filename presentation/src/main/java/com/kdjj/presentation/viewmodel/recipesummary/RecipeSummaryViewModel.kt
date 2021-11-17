package com.kdjj.presentation.viewmodel.recipesummary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.model.request.EmptyRequest
import com.kdjj.domain.model.request.GetLocalRecipeFlowRequest
import com.kdjj.domain.usecase.UseCase
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
    private var isInitialized = false
    
    fun initViewModel(recipeId: String) {
        if (isInitialized) return
        viewModelScope.launch {
            getRecipeFlowUseCase(GetLocalRecipeFlowRequest(recipeId))
                .onSuccess { recipeFlow ->
                    recipeFlow.collect { recipe ->
                        _liveRecipe.value = recipe
                    }
                }
        }
        isInitialized = true
    }
}
