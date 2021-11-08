package com.kdjj.domain.repository

import com.kdjj.domain.model.RecipeType

interface RecipeTypeRepository {
	
	suspend fun fetchRecipeTypes(): Result<List<RecipeType>>
	
	suspend fun saveRecipeType(): Result<Boolean>
	
	suspend fun getRecipeTypes(): Result<List<RecipeType>>
}