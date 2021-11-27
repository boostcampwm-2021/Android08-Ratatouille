package com.kdjj.local.dataSource

import androidx.room.withTransaction
import com.kdjj.data.datasource.RecipeTempLocalDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.local.dao.RecipeTempDao
import com.kdjj.local.dao.UselessImageDao
import com.kdjj.local.database.RecipeDatabase
import com.kdjj.local.dto.UselessImageDto
import com.kdjj.local.dto.toDomain
import com.kdjj.local.dto.toTempDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RecipeTempLocalDataSourceImpl @Inject constructor(
    private val recipeTempDao: RecipeTempDao,
    private val recipeDatabase: RecipeDatabase,
    private val uselessImageDao: UselessImageDao
) : RecipeTempLocalDataSource {

    override suspend fun saveRecipeTemp(recipe: Recipe): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                recipeDatabase.withTransaction {
                    uselessImageDao.deleteUselessImage(
                        listOf(recipe.imgPath) + recipe.stepList.map { it.imgPath }
                    )

                    removeImageByRecipeId(recipe.recipeId)

                    recipeTempDao.deleteTempStepList(recipe.recipeId)
                    recipeTempDao.insertRecipeTempMeta(recipe.toTempDto())
                    recipe.stepList.forEachIndexed { index, recipeStep ->
                        recipeTempDao.insertRecipeTempStep(recipeStep.toTempDto(recipe.recipeId, index + 1))
                    }
                }
            }
        }

    override suspend fun deleteRecipeTemp(recipeId: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                recipeDatabase.withTransaction {
                    removeImageByRecipeId(recipeId)
                    recipeTempDao.deleteRecipeTemp(recipeId)
                }
            }
        }

    override suspend fun getRecipeTemp(recipeId: String): Result<Recipe?> =
        withContext(Dispatchers.IO) {
            runCatching {
                recipeTempDao.getRecipeTemp(recipeId)?.toDomain()
            }
        }

    private suspend fun removeImageByRecipeId(recipeId: String) {
        recipeTempDao.getRecipeTemp(recipeId)
            ?.toDomain()
            ?.let { temp ->
                uselessImageDao.insertUselessImage(
                    (listOf(temp.imgPath) + temp.stepList.map { it.imgPath })
                        .filter { it.isNotEmpty() }
                        .map { UselessImageDto(it) }
                )
            }
    }
}