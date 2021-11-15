package com.kdjj.presentation.viewmodel.recipedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeStep
import javax.inject.Inject


class RecipeDetailViewModel @Inject constructor(
    // TODO("fetchLocalRecipeById")
) : ViewModel() {

    private val _liveStepList = MutableLiveData<List<RecipeStep>>()
    val liveStepList: LiveData<List<RecipeStep>> get() = _liveStepList

    fun initializeWith(recipe: Recipe) {
        _liveStepList.value = recipe.stepList
    }
}