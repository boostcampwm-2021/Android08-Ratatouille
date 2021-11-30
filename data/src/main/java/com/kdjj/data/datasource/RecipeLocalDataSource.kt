package com.kdjj.data.datasource

import com.kdjj.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeLocalDataSource {

    suspend fun saveRecipe(
        recipe: Recipe
    ): Result<Unit>

    suspend fun updateRecipe(
        recipe: Recipe
    ): Result<Unit>

    suspend fun deleteRecipe(
        recipe: Recipe
    ): Result<Unit>

    fun getRecipeFlow(
        recipeId: String
    ): Flow<Recipe>

    suspend fun getRecipe(
        recipeId: String
    ): Result<Recipe>

    suspend fun updateRecipe(
        recipe: Recipe,
        originImgPathList: List<String>
    ): Result<Unit>
}
