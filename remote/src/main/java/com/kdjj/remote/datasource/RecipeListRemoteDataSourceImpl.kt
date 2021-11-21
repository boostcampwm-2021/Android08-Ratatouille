package com.kdjj.remote.datasource

import com.google.firebase.firestore.FirebaseFirestoreException
import com.kdjj.data.common.errorMap
import com.kdjj.data.datasource.RecipeListRemoteDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.exception.ApiException
import com.kdjj.domain.model.exception.NetworkException
import com.kdjj.remote.dao.RemoteRecipeListService
import java.lang.Exception
import javax.inject.Inject

internal class RecipeListRemoteDataSourceImpl @Inject constructor(
    private val recipeListService: RemoteRecipeListService,
) : RecipeListRemoteDataSource {

    override suspend fun fetchLatestRecipeListAfter(
        refresh: Boolean
    ): Result<List<Recipe>> =
        runCatching {
            recipeListService.fetchLatestRecipeListAfter(refresh)
        }.errorMap {
            fireStoreExceptionToDomain(it)
        }
    
    override suspend fun fetchPopularRecipeListAfter(
        refresh: Boolean
    ): Result<List<Recipe>> =
        runCatching {
            recipeListService.fetchPopularRecipeListAfter(refresh)
        }.errorMap {
            fireStoreExceptionToDomain(it)
        }
    
    override suspend fun fetchSearchRecipeListAfter(
        keyword: String,
        refresh: Boolean
    ): Result<List<Recipe>> =
        runCatching {
            recipeListService.fetchSearchRecipeListAfter(keyword, refresh)
        }.errorMap {
            fireStoreExceptionToDomain(it)
        }

    private fun fireStoreExceptionToDomain(throwable: Throwable) =
        when (throwable) {
            is FirebaseFirestoreException -> {
                when (throwable.code.value()) {
                    14 -> NetworkException()
                    else -> ApiException()
                }
            }
            else -> {
                Exception(throwable)
            }
        }
}
