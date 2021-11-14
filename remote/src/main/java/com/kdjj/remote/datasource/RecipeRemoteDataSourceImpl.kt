package com.kdjj.remote.datasource

import com.kdjj.data.datasource.RecipeRemoteDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.remote.dao.RemoteRecipeDao
import javax.inject.Inject

internal class RecipeRemoteDataSourceImpl @Inject constructor(
    private val recipeDao: RemoteRecipeDao
) : RecipeRemoteDataSource {
    
    override suspend fun uploadRecipe(recipe: Recipe): Result<Unit> =
        try {
            recipeDao.uploadRecipe(recipe)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
}
