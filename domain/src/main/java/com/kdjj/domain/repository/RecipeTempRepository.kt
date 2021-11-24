package com.kdjj.domain.repository

import com.kdjj.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

interface RecipeTempRepository {

    suspend fun saveRecipeTemp(
        recipe: Recipe
    ): Result<Unit>

    suspend fun deleteRecipeTemp(
        recipeId: String
    ): Result<Unit>

    suspend fun getRecipeTemp(
        recipeId: String
    ): Result<Recipe?>
}
