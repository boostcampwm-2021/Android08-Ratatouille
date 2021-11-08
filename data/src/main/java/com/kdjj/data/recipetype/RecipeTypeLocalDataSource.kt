package com.kdjj.data.recipetype

import com.kdjj.domain.model.RecipeType

interface RecipeTypeLocalDataSource {
	
	suspend fun saveRecipeType():Result<Boolean>
	
	suspend fun getRecipeTypes(): Result<List<RecipeType>>
}