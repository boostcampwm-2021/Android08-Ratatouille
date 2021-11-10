package com.kdjj.local.dataSource

import com.kdjj.data.recipe.RecipeLocalDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.local.dao.RecipeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeLocalDataSourceImpl(
	private val recipeDao: RecipeDao
) : RecipeLocalDataSource {
	
	override suspend fun saveRecipe(recipe: Recipe): Result<Boolean> =
		withContext(Dispatchers.IO) {
			try {
				recipeDao.deleteStepList(recipe.recipeId)
				recipeDao.insertRecipe(recipe)
				Result.success(true)
			} catch (e: Exception) {
				Result.failure(Exception(e.message))
			}
		}
}
