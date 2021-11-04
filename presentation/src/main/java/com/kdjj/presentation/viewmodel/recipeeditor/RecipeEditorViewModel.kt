package com.kdjj.presentation.viewmodel.recipeeditor

import androidx.lifecycle.ViewModel
import com.kdjj.domain.usecase.FetchRecipeTypesUseCase
import com.kdjj.domain.usecase.SaveRecipeUseCase
import javax.inject.Inject

class RecipeEditorViewModel @Inject constructor(
    private val fetchRecipeTypes: FetchRecipeTypesUseCase,
    private val saveRecipe: SaveRecipeUseCase
) : ViewModel() {
}