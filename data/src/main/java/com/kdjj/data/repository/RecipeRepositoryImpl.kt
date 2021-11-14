package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeLocalDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

internal class RecipeRepositoryImpl @Inject constructor(
    private val recipeLocalDataSource: RecipeLocalDataSource
) : RecipeRepository {
    
    override suspend fun saveRecipe(
        recipe: Recipe
    ): Result<Boolean> {
        return recipeLocalDataSource.saveRecipe(recipe)
    }
    
    override suspend fun updateRecipe(
        recipe: Recipe
    ): Result<Boolean> {
        return recipeLocalDataSource.updateRecipe(recipe)
    }
    
    override suspend fun deleteRecipe(
        recipe: Recipe
    ): Result<Boolean> {
        return recipeLocalDataSource.deleteRecipe(recipe)
    }
}
