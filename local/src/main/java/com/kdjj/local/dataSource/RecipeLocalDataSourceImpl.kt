package com.kdjj.local.dataSource

import com.kdjj.data.common.errorMap
import com.kdjj.data.datasource.RecipeLocalDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.local.dao.RecipeDao
import com.kdjj.local.dto.toDomain
import com.kdjj.local.dto.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RecipeLocalDataSourceImpl @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipeLocalDataSource {

    override suspend fun saveRecipe(
        recipe: Recipe
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            runCatching {
                recipeDao.deleteStepList(recipe.recipeId)
                recipeDao.insertRecipe(recipe)
                true
            }.errorMap {
                Exception(it.message)
            }
        }

    override suspend fun updateRecipe(
        recipe: Recipe
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            runCatching {
                recipeDao.updateRecipeMeta(recipe.toDto())
                true
            }.errorMap {
                Exception(it.message)
            }
        }

    override suspend fun deleteRecipe(
        recipe: Recipe
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            runCatching {
                recipeDao.deleteRecipe(recipe.toDto())
                true
            }.errorMap {
                Exception(it.message)
            }
        }

    override fun getRecipeFlow(
        recipeId: String
    ): Flow<Recipe> =
        recipeDao.getRecipe(recipeId)
                .map { it.toDomain() }
}
