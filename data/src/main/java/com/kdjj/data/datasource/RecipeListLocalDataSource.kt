package com.kdjj.data.datasource

import com.kdjj.domain.model.Recipe

interface RecipeListLocalDataSource {

    suspend fun fetchLatestRecipeListAfter(page: Int): Result<List<Recipe>>
}