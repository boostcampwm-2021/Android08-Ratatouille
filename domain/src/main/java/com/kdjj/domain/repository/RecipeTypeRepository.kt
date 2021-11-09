package com.kdjj.domain.repository

import com.kdjj.domain.model.RecipeType

interface RecipeTypeRepository {
	
	suspend fun fetchRemoteRecipeTypes(): Result<List<RecipeType>>
	
	suspend fun fetchLocalRecipeTypes(): Result<List<RecipeType>>
	
	suspend fun saveRecipeType(recipeTypeList: List<RecipeType>): Result<Boolean>
}
