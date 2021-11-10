package com.kdjj.local.dataSource

import com.kdjj.data.recipe.RecipeLocalDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.local.dao.RecipeDao
import com.kdjj.local.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeLocalDataSourceImpl(
	private val recipeDatabase: RecipeDao
) : RecipeLocalDataSource {
	
	override suspend fun saveRecipe(recipe: Recipe): Result<Boolean> =
		withContext(Dispatchers.IO) {
			try {
				recipeDatabase.insertRecipeMeta(recipe.toEntity())
				recipe.stepList.forEachIndexed { idx, recipeStep ->
					recipeDatabase.insertRecipeStep(
						recipeStep.toEntity(recipe.recipeId, idx + 1)
					)
				}
				Result.success(true)
			} catch (e: Exception) {
				Result.failure(Exception(e.message))
			}
		}
}
