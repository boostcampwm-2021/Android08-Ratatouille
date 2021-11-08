package com.kdjj.data.datasource

import com.kdjj.domain.model.Recipe

interface RecipeListRemoteDataSource {

    suspend fun fetchLatestRecipeList(): Result<List<Recipe>>
}