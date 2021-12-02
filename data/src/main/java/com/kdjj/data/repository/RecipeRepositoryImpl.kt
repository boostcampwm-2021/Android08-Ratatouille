package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeLocalDataSource
import com.kdjj.data.datasource.RecipeRemoteDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

internal class RecipeRepositoryImpl @Inject constructor(
    private val recipeLocalDataSource: RecipeLocalDataSource,
    private val recipeRemoteDataSource: RecipeRemoteDataSource
) : RecipeRepository {

    private val isUpdatedFlow = MutableSharedFlow<Unit>(0)

    override suspend fun saveMyRecipe(
        recipe: Recipe
    ): Result<Unit> {
        return recipeLocalDataSource.saveRecipe(recipe).onSuccess {
            coroutineScope {
                isUpdatedFlow.emit(Unit)
            }
        }
    }

    override suspend fun updateMyRecipe(
        recipe: Recipe
    ): Result<Unit> {
        return recipeLocalDataSource.updateRecipe(recipe).onSuccess {
            coroutineScope {
                isUpdatedFlow.emit(Unit)
            }
        }
    }

    override suspend fun updateMyRecipe(
        recipe: Recipe,
        originImgPathList: List<String>
    ): Result<Unit> {
        return recipeLocalDataSource.updateRecipe(recipe, originImgPathList).onSuccess {
            coroutineScope {
                isUpdatedFlow.emit(Unit)
            }
        }
    }

    override suspend fun deleteMyRecipe(
        recipe: Recipe
    ): Result<Unit> {
        return recipeLocalDataSource.deleteRecipe(recipe).onSuccess {
            coroutineScope {
                isUpdatedFlow.emit(Unit)
            }
        }
    }

    override suspend fun uploadRecipe(
        recipe: Recipe
    ): Result<Unit> {
        return recipeRemoteDataSource.uploadRecipe(recipe)
    }

    override suspend fun increaseOthersRecipeViewCount(
        recipe: Recipe
    ): Result<Unit> {
        return recipeRemoteDataSource.increaseViewCount(recipe)
    }

    override suspend fun deleteOthersRecipe(
        recipe: Recipe
    ): Result<Unit> {
        return recipeRemoteDataSource.deleteRecipe(recipe)
    }

    override fun getMyRecipeFlow(
        recipeId: String
    ): Flow<Recipe> {
        return recipeLocalDataSource.getRecipeFlow(recipeId)
    }

    override suspend fun fetchOthersRecipe(recipeId: String): Result<Recipe> {
        return recipeRemoteDataSource.fetchRecipe(recipeId)
    }

    override fun getRecipeUpdateFlow(): Flow<Unit> {
        return isUpdatedFlow
    }

    override suspend fun getLocalRecipe(
        recipeId: String
    ): Result<Recipe> {
        return recipeLocalDataSource.getRecipe(recipeId)
    }
}
