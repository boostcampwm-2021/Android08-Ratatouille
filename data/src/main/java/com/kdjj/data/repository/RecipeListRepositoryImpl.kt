package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeListRemoteDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeListRepository
import javax.inject.Inject

class RecipeListRepositoryImpl @Inject constructor(
    private val recipeListRemoteDataSource: RecipeListRemoteDataSource,
) : RecipeListRepository {

    override suspend fun fetchRemoteLatestRecipeListAfter(lastVisibleCreateTime: Long): Result<List<Recipe>> =
        recipeListRemoteDataSource.fetchLatestRecipeListAfter(lastVisibleCreateTime)

    override suspend fun fetchRemotePopularRecipeListAfter(lastVisibleViewCount: Int): Result<List<Recipe>> =
        recipeListRemoteDataSource.fetchPopularRecipeListAfter(lastVisibleViewCount)
}