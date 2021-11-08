package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeListRemoteDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeListRepository
import javax.inject.Inject

class RecipeListRepositoryImpl @Inject constructor(
    private val recipeListRemoteDataSource: RecipeListRemoteDataSource,
) : RecipeListRepository {

    override suspend fun fetchRemoteLatestRecipeList(lastVisibleCreateTime: Long): Result<List<Recipe>> =
        recipeListRemoteDataSource.fetchLatestRecipeList(lastVisibleCreateTime)
}