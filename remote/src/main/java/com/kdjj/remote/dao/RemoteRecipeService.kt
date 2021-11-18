package com.kdjj.remote.dao

import com.kdjj.domain.model.Recipe

internal interface RemoteRecipeService {
    
    suspend fun uploadRecipe(
        recipe: Recipe
    )
    
    suspend fun increaseViewCount(
        recipe: Recipe
    )
    
    suspend fun deleteRecipe(
        recipe: Recipe
    )

    suspend fun fetchRecipe(
        recipeID: String
    ): Recipe
}
