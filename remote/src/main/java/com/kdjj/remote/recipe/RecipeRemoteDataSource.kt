package com.kdjj.remote.recipe

import com.kdjj.domain.model.RecipeType

interface RecipeRemoteDataSource {

	suspend fun fetchRecipeTypes(): Result<List<RecipeType>>
}