package com.kdjj.domain.repository

import com.kdjj.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun saveMyRecipe(
        recipe: Recipe
    ): Result<Boolean>

    suspend fun updateMyRecipe(
        recipe: Recipe
    ): Result<Boolean>

    suspend fun deleteMyRecipe(
        recipe: Recipe
    ): Result<Boolean>

    suspend fun uploadRecipe(
        recipe: Recipe
    ): Result<Unit>

    suspend fun increaseOthersRecipeViewCount(
        recipe: Recipe
    ): Result<Unit>

    suspend fun deleteOthersRecipe(
        recipe: Recipe
    ): Result<Unit>

    fun getMyRecipeFlow(
        recipeId: String
    ): Flow<Recipe>

    suspend fun fetchOthersRecipe(
        recipeId: String
    ): Result<Recipe>

    fun getRecipeUpdateFlow(): Flow<Int>

    suspend fun getLocalRecipe(
        recipeId: String
    ): Result<Recipe>

    suspend fun updateMyRecipe(
        recipe: Recipe,
        originImgPathList: List<String>
    ): Result<Unit>
}
