package com.kdjj.remote

import com.kdjj.domain.model.RecipeType

interface RecipeRemoteDataSource {

	suspend fun fetchRecipeTypes(): Result<List<RecipeType>>
}