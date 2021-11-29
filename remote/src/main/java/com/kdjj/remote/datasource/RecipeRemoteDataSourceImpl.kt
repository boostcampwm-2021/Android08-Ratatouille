package com.kdjj.remote.datasource

import com.kdjj.domain.common.errorMap
import com.kdjj.data.datasource.RecipeRemoteDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.remote.common.fireStoreExceptionToDomain
import com.kdjj.remote.dto.toDomain
import com.kdjj.remote.dto.toDto
import com.kdjj.remote.service.RemoteRecipeService
import javax.inject.Inject

internal class RecipeRemoteDataSourceImpl @Inject constructor(
    private val recipeService: RemoteRecipeService
) : RecipeRemoteDataSource {

    override suspend fun uploadRecipe(recipe: Recipe): Result<Unit> =
        runCatching {
            recipeService.uploadRecipe(recipe.toDto())
        }

    override suspend fun increaseViewCount(recipe: Recipe): Result<Unit> =
        runCatching {
            recipeService.increaseViewCount(recipe.toDto())
        }

    override suspend fun deleteRecipe(recipe: Recipe): Result<Unit> =
        runCatching {
            recipeService.deleteRecipe(recipe.toDto())
        }

    override suspend fun fetchRecipe(recipeID: String): Result<Recipe> =
        runCatching {
            recipeService.fetchRecipe(recipeID).toDomain()
        }.errorMap {
            fireStoreExceptionToDomain(it)
        }
}
