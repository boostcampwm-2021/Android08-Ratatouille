package com.kdjj.local.dataSource

import androidx.room.withTransaction
import com.kdjj.data.common.errorMap
import com.kdjj.data.datasource.RecipeLocalDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.local.database.RecipeDatabase
import com.kdjj.local.dto.toDomain
import com.kdjj.local.dto.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RecipeLocalDataSourceImpl @Inject constructor(
    private val recipeDatabase: RecipeDatabase
) : RecipeLocalDataSource {

    private val recipeDao = recipeDatabase.getRecipeDao()
    private val recipeImageValidationDao = recipeDatabase.getImageValidationDao()

    override suspend fun saveRecipe(
        recipe: Recipe
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            runCatching {
                recipeDatabase.withTransaction {
                    recipeImageValidationDao.updateValidate(
                        recipe.stepList.map { it.imgPath }.plus(recipe.imgPath),
                        true
                    )
                    recipeDao.deleteStepList(recipe.recipeId)
                    recipeDao.insertRecipeMeta(recipe.toDto())
                    recipe.stepList.forEachIndexed { idx, recipeStep ->
                        recipeDao.insertRecipeStep(recipeStep.toDto(recipe.recipeId, idx + 1))
                    }
                }
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

    override suspend fun updateRecipe(
        recipe: Recipe,
        originImgPathList: List<String>
    ): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                recipeDatabase.withTransaction {
                    recipeImageValidationDao.updateValidate(
                        recipe.stepList.map { it.imgPath }.plus(recipe.imgPath),
                        true
                    )
                    recipeImageValidationDao.updateValidate(
                        originImgPathList,
                        false
                    )
                    recipeDao.deleteStepList(recipe.recipeId)
                    recipeDao.insertRecipeMeta(recipe.toDto())
                    recipe.stepList.forEachIndexed { idx, recipeStep ->
                        recipeDao.insertRecipeStep(recipeStep.toDto(recipe.recipeId, idx + 1))
                    }
                }
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

    override suspend fun getRecipe(recipeId: String): Result<Recipe> =
        withContext(Dispatchers.IO) {
            runCatching {
                recipeDao.getRecipeDto(recipeId).toDomain()
            }
        }
}
