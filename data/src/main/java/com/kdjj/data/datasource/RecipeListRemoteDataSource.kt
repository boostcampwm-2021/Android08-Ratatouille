package com.kdjj.data.datasource

import com.kdjj.domain.model.Recipe

interface RecipeListRemoteDataSource {

    suspend fun fetchLatestRecipeListAfter(lastVisibleCreateTime: Long): Result<List<Recipe>>

    suspend fun fetchPopularRecipeListAfter(lastVisibleViewCount: Int): Result<List<Recipe>>
}