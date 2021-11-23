package com.kdjj.local.dataSource

import com.kdjj.data.datasource.RecipeTempLocalDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.local.dao.RecipeTempDao
import com.kdjj.local.dto.toDomain
import com.kdjj.local.dto.toTempDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RecipeTempLocalDataSourceImpl @Inject constructor(
    private val recipeTempDao: RecipeTempDao
) : RecipeTempLocalDataSource {

    override suspend fun saveRecipeTemp(recipe: Recipe): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                recipeTempDao.insertRecipeTemp(recipe)
            }
        }

    override suspend fun deleteRecipeTemp(recipe: Recipe): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                recipeTempDao.deleteRecipeTemp(recipe.toTempDto())
            }
        }

    override suspend fun getRecipeTemp(recipeId: String): Result<Recipe?> =
        withContext(Dispatchers.IO) {
            runCatching {
                recipeTempDao.getRecipeTemp(recipeId)?.toDomain()
            }
        }
}