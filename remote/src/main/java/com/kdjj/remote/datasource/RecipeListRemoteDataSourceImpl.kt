package com.kdjj.remote.datasource

import com.kdjj.domain.common.errorMap
import com.kdjj.data.datasource.RecipeListRemoteDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.remote.common.fireStoreExceptionToDomain
import com.kdjj.remote.dto.toDomain
import com.kdjj.remote.service.RemoteRecipeListService
import javax.inject.Inject

internal class RecipeListRemoteDataSourceImpl @Inject constructor(
    private val recipeListService: RemoteRecipeListService,
) : RecipeListRemoteDataSource {

    override suspend fun fetchLatestRecipeListAfter(
        refresh: Boolean
    ): Result<List<Recipe>> =
        runCatching {
            recipeListService.fetchLatestRecipeListAfter(refresh)
                .map { it.toDomain() }
        }.errorMap {
            fireStoreExceptionToDomain(it)
        }

    override suspend fun fetchPopularRecipeListAfter(
        refresh: Boolean
    ): Result<List<Recipe>> =
        runCatching {
            recipeListService.fetchPopularRecipeListAfter(refresh)
                .map { it.toDomain() }
        }.errorMap {
            fireStoreExceptionToDomain(it)
        }

    override suspend fun fetchSearchRecipeListAfter(
        keyword: String,
        refresh: Boolean
    ): Result<List<Recipe>> =
        runCatching {
            recipeListService.fetchSearchRecipeListAfter(keyword, refresh)
                .map { it.toDomain() }
        }.errorMap {
            fireStoreExceptionToDomain(it)
        }
}
