package com.kdjj.domain.repository

import com.kdjj.domain.model.Recipe

interface RecipeRepository {
    
    suspend fun saveRecipe(
        recipe: Recipe
    ): Result<Boolean>
    
    suspend fun updateRecipe(
        recipe: Recipe
    ): Result<Boolean>
    
    suspend fun deleteLocalRecipe(
        recipe: Recipe
    ): Result<Boolean>
    
    suspend fun uploadRecipe(
        recipe: Recipe
    ): Result<Unit>
}
