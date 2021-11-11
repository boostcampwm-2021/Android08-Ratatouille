package com.kdjj.data.datasource

import com.kdjj.domain.model.RecipeType

interface RecipeTypeRemoteDataSource {
	
	suspend fun fetchRecipeTypeList(): Result<List<RecipeType>>
}
