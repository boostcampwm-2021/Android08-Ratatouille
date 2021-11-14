package com.kdjj.domain.repository

import com.kdjj.domain.model.Recipe

interface RecipeRepository {
    
    suspend fun saveLocalRecipe(
        recipe: Recipe
    ): Result<Boolean>
    
    suspend fun updateLocalRecipe(
        recipe: Recipe
    ): Result<Boolean>
    
    suspend fun deleteLocalRecipe(
        recipe: Recipe
    ): Result<Boolean>
    
    suspend fun uploadRecipe(
        recipe: Recipe
    ): Result<Unit>
    
    suspend fun increaseRemoteRecipeViewCount(
        recipe: Recipe
    ): Result<Unit>
    
    suspend fun deleteRemoteRecipe(
        recipe: Recipe
    ): Result<Unit>
}
