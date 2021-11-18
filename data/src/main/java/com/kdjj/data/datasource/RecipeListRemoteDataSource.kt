package com.kdjj.data.datasource

import com.kdjj.domain.model.Recipe

interface RecipeListRemoteDataSource {
    
    suspend fun fetchLatestRecipeListAfter(
        refresh: Boolean
    ): Result<List<Recipe>>
    
    suspend fun fetchPopularRecipeListAfter(
        refresh: Boolean
    ): Result<List<Recipe>>
    
    suspend fun fetchSearchRecipeListAfter(
        keyword: String,
        lastVisibleTitle: String
    ): Result<List<Recipe>>
}
