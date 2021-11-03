package com.kdjj.presentation.viewmodel.recipeeditor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.kdjj.domain.usecase.FetchRecipeTypesUseCase
import com.kdjj.domain.usecase.SaveRecipeUseCase
import javax.inject.Inject

class RecipeEditorViewModel @Inject constructor(
    private val fetchRecipeTypes: FetchRecipeTypesUseCase,
    private val saveRecipe: SaveRecipeUseCase
) : ViewModel() {

    //livedata naming convention
    val liveTitle = MutableLiveData<String>()
    val liveTitleState = liveTitle.switchMap {
        MutableLiveData(validateTitle(it))
    }

    private fun validateTitle(title: String): Boolean {
        // to-do
        return true
    }
}