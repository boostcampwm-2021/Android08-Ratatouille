package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeLocalDataSource
import com.kdjj.data.datasource.RecipeRemoteDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

internal class RecipeRepositoryImpl @Inject constructor(
    private val recipeLocalDataSource: RecipeLocalDataSource,
    private val recipeRemoteDataSource: RecipeRemoteDataSource
) : RecipeRepository {

    private val isUpdated = MutableStateFlow(0)

    override suspend fun saveLocalRecipe(
        recipe: Recipe
    ): Result<Boolean> {
        return recipeLocalDataSource.saveRecipe(recipe).onSuccess { isUpdated.value++ }
    }

    override suspend fun updateLocalRecipe(
        recipe: Recipe
    ): Result<Boolean> {
        return recipeLocalDataSource.updateRecipe(recipe).onSuccess { isUpdated.value++ }
    }

    override suspend fun deleteLocalRecipe(
        recipe: Recipe
    ): Result<Boolean> {
        return recipeLocalDataSource.deleteRecipe(recipe).onSuccess { isUpdated.value++ }
    }

    override suspend fun uploadRecipe(
        recipe: Recipe
    ): Result<Unit> {
        return recipeRemoteDataSource.uploadRecipe(recipe)
    }

    override suspend fun increaseRemoteRecipeViewCount(
        recipe: Recipe
    ): Result<Unit> {
        return recipeRemoteDataSource.increaseViewCount(recipe)
    }

    override suspend fun deleteRemoteRecipe(
        recipe: Recipe
    ): Result<Unit> {
        return recipeRemoteDataSource.deleteRecipe(recipe)
    }

    override fun getLocalRecipeFlow(
        recipeId: String
    ): Flow<Recipe> {
        return recipeLocalDataSource.getRecipeFlow(recipeId)
    }

    override suspend fun fetchRemoteRecipe(recipeId: String): Result<Recipe> {
        return recipeRemoteDataSource.fetchRecipe(recipeId)
    }

    override fun getRecipeUpdateFlow(): Flow<Int> {
        return isUpdated
    }

    override suspend fun getLocalRecipe(recipeId: String): Result<Recipe> {
        return recipeLocalDataSource.getRecipe(recipeId)
    }
}
