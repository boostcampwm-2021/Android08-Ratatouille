package com.kdjj.data.recipetype

import com.kdjj.domain.model.RecipeType

interface RecipeTypeLocalDataSource {
	
	suspend fun saveRecipeTypeList(recipeTypeList: List<RecipeType>): Result<Boolean>
	
	suspend fun fetchRecipeTypeList(): Result<List<RecipeType>>
}
