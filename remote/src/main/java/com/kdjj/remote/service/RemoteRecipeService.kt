package com.kdjj.remote.service

import com.kdjj.domain.model.Recipe
import com.kdjj.remote.dto.RecipeDto

internal interface RemoteRecipeService {
    
    suspend fun uploadRecipe(
        recipeDto: RecipeDto
    )
    
    suspend fun increaseViewCount(
        recipeDto: RecipeDto
    )
    
    suspend fun deleteRecipe(
        recipeDto: RecipeDto
    )

    suspend fun fetchRecipe(
        recipeId: String
    ): RecipeDto
}
