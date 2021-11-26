package com.kdjj.remote.datasource

import com.kdjj.data.datasource.RecipeImageRemoteDataSource
import com.kdjj.remote.service.FirebaseStorageService
import javax.inject.Inject

internal class RecipeImageRemoteDataSourceImpl @Inject constructor(
    private val firebaseStorageService: FirebaseStorageService
) : RecipeImageRemoteDataSource {
    
    override suspend fun fetchRecipeImage(
        uriList: List<String>
    ): Result<List<ByteArray>> {
        return firebaseStorageService.fetchRecipeImage(uriList)
    }
    
    override suspend fun uploadRecipeImage(
        uri: String
    ): Result<String> {
        return firebaseStorageService.uploadRecipeImage(uri)
    }
}
