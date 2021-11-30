package com.kdjj.remote.datasource

import com.kdjj.data.datasource.RecipeImageRemoteDataSource
import com.kdjj.domain.common.errorMap
import com.kdjj.remote.service.FirebaseStorageService
import javax.inject.Inject

internal class RecipeImageRemoteDataSourceImpl @Inject constructor(
    private val firebaseStorageService: FirebaseStorageService
) : RecipeImageRemoteDataSource {

    override suspend fun fetchRecipeImage(
        uriList: List<String>
    ): Result<List<ByteArray>> =
        runCatching {
            firebaseStorageService.fetchRecipeImage(uriList)
        }.errorMap {
            Exception(it.message)
        }


    override suspend fun uploadRecipeImage(
        uri: String
    ): Result<String> =
        runCatching {
            firebaseStorageService.uploadRecipeImage(uri)
        }.errorMap {
            Exception(it.message)
        }
}

