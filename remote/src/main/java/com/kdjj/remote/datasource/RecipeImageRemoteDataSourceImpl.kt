package com.kdjj.remote.datasource

import com.kdjj.data.datasource.RecipeImageRemoteDataSource
import com.kdjj.remote.dao.FirebaseStorageService
import javax.inject.Inject

internal class RecipeImageRemoteDataSourceImpl @Inject constructor(
    private val firebaseStorageService: FirebaseStorageService
) : RecipeImageRemoteDataSource {
    
    override suspend fun fetchRecipeImage(
        uri: String
    ): Result<ByteArray> {
        return firebaseStorageService.fetchRecipeImage(uri)
    }
    
    override suspend fun uploadRecipeImage(
        uri: String
    ): Result<String> {
        return firebaseStorageService.uploadRecipeImage(uri)
    }
}
