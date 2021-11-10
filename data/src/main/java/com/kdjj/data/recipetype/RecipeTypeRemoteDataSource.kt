package com.kdjj.data.recipetype

import com.kdjj.domain.model.RecipeType

interface RecipeTypeRemoteDataSource {
	
	suspend fun fetchRecipeTypeList(): Result<List<RecipeType>>
}
