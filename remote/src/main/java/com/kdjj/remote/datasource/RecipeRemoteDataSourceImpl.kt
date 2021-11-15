package com.kdjj.remote.datasource

import com.kdjj.data.datasource.RecipeRemoteDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.remote.dao.RemoteRecipeService
import javax.inject.Inject

internal class RecipeRemoteDataSourceImpl @Inject constructor(
    private val recipeService: RemoteRecipeService
) : RecipeRemoteDataSource {
    
    override suspend fun uploadRecipe(recipe: Recipe): Result<Unit> =
        try {
            recipeService.uploadRecipe(recipe)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    
    override suspend fun increaseViewCount(recipe: Recipe): Result<Unit> =
        try {
            recipeService.increaseViewCount(recipe)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    
    override suspend fun deleteRecipe(recipe: Recipe): Result<Unit> =
        try {
            recipeService.deleteRecipe(recipe)
            Result.success(Unit)
        }catch(e: Exception){
            Result.failure(e)
        }
}
