package com.kdjj.remote.datasource

import com.kdjj.data.datasource.RecipeImageRemoteDataSource
import com.kdjj.remote.dao.FirebaseStorageDao
import javax.inject.Inject

internal class RecipeImageRemoteDataSourceImpl @Inject constructor(
    private val firebaseStorageDao: FirebaseStorageDao
) : RecipeImageRemoteDataSource {
    
    override suspend fun fetchRecipeImage(
        uri: String
    ): Result<ByteArray> {
        return firebaseStorageDao.fetchRecipeImage(uri)
    }
    
    override suspend fun uploadRecipeImage(
        uri: String
    ): Result<String> {
        return firebaseStorageDao.uploadRecipeImage(uri)
    }
}
