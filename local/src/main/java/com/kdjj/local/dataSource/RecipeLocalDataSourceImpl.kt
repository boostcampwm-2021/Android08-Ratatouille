package com.kdjj.local.dataSource

import com.kdjj.data.datasource.RecipeLocalDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.local.dao.RecipeDao
import com.kdjj.local.dto.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RecipeLocalDataSourceImpl @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipeLocalDataSource {
    
    override suspend fun saveRecipe(
        recipe: Recipe
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                recipeDao.deleteStepList(recipe.recipeId)
                recipeDao.insertRecipe(recipe)
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception(e.message))
            }
        }
    
    override suspend fun updateRecipe(
        recipe: Recipe
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                recipeDao.updateRecipeMeta(recipe.toEntity())
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception(e.message))
            }
        }
    
    override suspend fun deleteRecipe(
        recipe: Recipe
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                recipeDao.deleteRecipe(recipe.toEntity())
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception(e.message))
            }
        }
}
