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
        try {
            val recipeList = recipeListService.fetchLatestRecipeListAfter(lastVisibleCreateTime)
            Result.success(recipeList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    
    override suspend fun fetchPopularRecipeListAfter(
        lastVisibleViewCount: Int
    ): Result<List<Recipe>> =
        try {
            val recipeList = recipeListService.fetchPopularRecipeListAfter(lastVisibleViewCount)
            Result.success(recipeList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    
    override suspend fun fetchSearchRecipeListAfter(
        keyword: String,
        lastVisibleTitle: String
    ): Result<List<Recipe>> =
        try {
            val recipeList = recipeListService.fetchSearchRecipeListAfter(keyword, lastVisibleTitle)
            Result.success(recipeList)
        } catch (e: Exception) {
            Result.failure(e)
        }
}
