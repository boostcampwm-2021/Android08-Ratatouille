package com.kdjj.remote.dao

import com.kdjj.domain.model.Recipe

internal interface RemoteRecipeDao {
    
    suspend fun uploadRecipe(
        recipe: Recipe
    )
    
    suspend fun increaseViewCount(
        recipe: Recipe
    )
    
    suspend fun deleteRecipe(
        recipe: Recipe
    )
}
