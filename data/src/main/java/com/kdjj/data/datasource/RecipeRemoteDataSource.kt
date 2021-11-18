package com.kdjj.data.datasource

import com.kdjj.domain.model.Recipe

interface RecipeRemoteDataSource {

    suspend fun uploadRecipe(
        recipe: Recipe
    ): Result<Unit>

    suspend fun increaseViewCount(
        recipe: Recipe
    ): Result<Unit>

    suspend fun deleteRecipe(
        recipe: Recipe
    ): Result<Unit>

    suspend fun fetchRecipe(
        recipeID: String
    ): Result<Recipe>
}
