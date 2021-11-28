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
    
    override suspend fun fetchOthersLatestRecipeListAfter(
        refresh: Boolean
    ): Result<List<Recipe>> =
        recipeListRemoteDataSource.fetchLatestRecipeListAfter(refresh)

    override suspend fun fetchOthersPopularRecipeListAfter(
        refresh: Boolean
    ): Result<List<Recipe>> =
        recipeListRemoteDataSource.fetchPopularRecipeListAfter(refresh)

    override suspend fun fetchOthersSearchRecipeListAfter(
        keyword: String,
        refresh: Boolean
    ): Result<List<Recipe>> =
        recipeListRemoteDataSource.fetchSearchRecipeListAfter(keyword, refresh)

    override suspend fun fetchMyLatestRecipeListAfter(
        index: Int
    ): Result<List<Recipe>> =
        recipeListLocalDataSource.fetchLatestRecipeListAfter(index)

    override suspend fun fetchMyFavoriteRecipeListAfter(
        index: Int
    ): Result<List<Recipe>> =
        recipeListLocalDataSource.fetchFavoriteRecipeListAfter(index)

    override suspend fun fetchMySearchRecipeListAfter(
        keyword: String,
        index: Int
    ): Result<List<Recipe>> =
        recipeListLocalDataSource.fetchSearchRecipeListAfter(keyword, index)

    override suspend fun fetchMyTitleRecipeListAfter(
        index: Int
    ): Result<List<Recipe>> =
        recipeListLocalDataSource.fetchTitleListAfter(index)

}
