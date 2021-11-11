package com.kdjj.remote.recipe

import com.kdjj.data.recipeimage.RecipeImageRemoteDataSource
import com.kdjj.remote.FirebaseStorageDao
import javax.inject.Inject

internal class RecipeImageRemoteDataSourceImpl @Inject constructor(
    private val firebaseStorageDao: FirebaseStorageDao
): RecipeImageRemoteDataSource {

    override suspend fun fetchRecipeImage(uri: String): Result<ByteArray> {
        return firebaseStorageDao.fetchRecipeImage(uri)
    }

    override suspend fun uploadRecipeImage(uri: String): Result<String> {
        return firebaseStorageDao.uploadRecipeImage(uri)
    }
}
