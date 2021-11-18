package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeListLocalDataSource
import com.kdjj.data.datasource.RecipeListRemoteDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeListRepository
import javax.inject.Inject

internal class RecipeListRepositoryImpl @Inject constructor(
    private val recipeListRemoteDataSource: RecipeListRemoteDataSource,
    private val recipeListLocalDataSource: RecipeListLocalDataSource,
) : RecipeListRepository {
    
    override suspend fun fetchRemoteLatestRecipeListAfter(
        lastVisibleCreateTime: Long
    ): Result<List<Recipe>> =
        recipeListRemoteDataSource.fetchLatestRecipeListAfter(lastVisibleCreateTime)
    
    override suspend fun fetchRemotePopularRecipeListAfter(
        refresh: Boolean
    ): Result<List<Recipe>> =
        recipeListRemoteDataSource.fetchPopularRecipeListAfter(refresh)
    
    override suspend fun fetchRemoteSearchRecipeListAfter(
        keyword: String,
        lastVisibleTitle: String
    ): Result<List<Recipe>> =
        recipeListRemoteDataSource.fetchSearchRecipeListAfter(keyword, lastVisibleTitle)
    
    override suspend fun fetchLocalLatestRecipeListAfter(
        index: Int
    ): Result<List<Recipe>> =
        recipeListLocalDataSource.fetchLatestRecipeListAfter(index)
    
    override suspend fun fetchLocalFavoriteRecipeListAfter(
        index: Int
    ): Result<List<Recipe>> =
        recipeListLocalDataSource.fetchFavoriteRecipeListAfter(index)
    
    override suspend fun fetchLocalSearchRecipeListAfter(
        keyword: String,
        index: Int
    ): Result<List<Recipe>> =
        recipeListLocalDataSource.fetchSearchRecipeListAfter(keyword, index)

    override suspend fun fetchLocalTitleRecipeListAfter(
        index: Int
    ): Result<List<Recipe>> =
        recipeListLocalDataSource.fetchTitleListAfter(index)

}
