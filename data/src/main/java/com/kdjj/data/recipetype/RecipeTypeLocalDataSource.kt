package com.kdjj.data.recipetype

import com.kdjj.domain.model.RecipeType

interface RecipeTypeLocalDataSource {
	
	suspend fun saveRecipeType(recipeTypeList: List<RecipeType>): Result<Boolean>
	
	suspend fun fetchRecipeTypes(): Result<List<RecipeType>>
}
