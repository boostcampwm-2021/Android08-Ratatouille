package com.kdjj.remote.datasource

import com.kdjj.data.datasource.RecipeListRemoteDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.remote.dao.RemoteRecipeListService
import javax.inject.Inject

internal class RecipeListRemoteDataSourceImpl @Inject constructor(
    private val recipeListService: RemoteRecipeListService,
) : RecipeListRemoteDataSource {
    
    override suspend fun fetchLatestRecipeListAfter(
        lastVisibleCreateTime: Long
    ): Result<List<Recipe>> =
        runCatching {
            recipeListService.fetchLatestRecipeListAfter(lastVisibleCreateTime)
        }
    
    override suspend fun fetchPopularRecipeListAfter(
        refresh: Boolean
    ): Result<List<Recipe>> =
        runCatching {
            recipeListService.fetchPopularRecipeListAfter(refresh)
        }
    
    override suspend fun fetchSearchRecipeListAfter(
        keyword: String,
        lastVisibleTitle: String
    ): Result<List<Recipe>> =
        runCatching {
            recipeListService.fetchSearchRecipeListAfter(keyword, lastVisibleTitle)
        }
}
