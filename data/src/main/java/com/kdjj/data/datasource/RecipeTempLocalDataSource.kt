package com.kdjj.data.datasource

import com.kdjj.domain.model.Recipe

interface RecipeTempLocalDataSource {

    suspend fun saveRecipeTemp(
        recipe: Recipe
    ): Result<Unit>

    suspend fun deleteRecipeTemp(
        recipe: Recipe
    ): Result<Unit>

    suspend fun getRecipeTemp(
        recipeId: String
    ): Result<Recipe?>
}