package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeTempLocalDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeTempRepository
import javax.inject.Inject

class RecipeTempRepositoryImpl @Inject constructor(
    private val recipeTempLocalDataSource: RecipeTempLocalDataSource
) : RecipeTempRepository {

    override suspend fun saveRecipeTemp(recipe: Recipe): Result<Unit> =
        recipeTempLocalDataSource.saveRecipeTemp(recipe)

    override suspend fun deleteRecipeTemp(recipeId: String): Result<Unit> =
        recipeTempLocalDataSource.deleteRecipeTemp(recipeId)

    override suspend fun getRecipeTemp(recipeId: String): Result<Recipe?> =
        recipeTempLocalDataSource.getRecipeTemp(recipeId)
}