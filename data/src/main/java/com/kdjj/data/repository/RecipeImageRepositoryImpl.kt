package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeImageLocalDataSource
import com.kdjj.data.datasource.RecipeImageRemoteDataSource
import com.kdjj.domain.repository.RecipeImageRepository
import javax.inject.Inject

internal class RecipeImageRepositoryImpl @Inject constructor(
    private val recipeImageLocalDataSource: RecipeImageLocalDataSource,
    private val recipeImageRemoteDataSource: RecipeImageRemoteDataSource
) : RecipeImageRepository {
    
    override suspend fun convertRemoteUriToByteArray(
        uri: String
    ): Result<ByteArray> {
        return recipeImageRemoteDataSource.fetchRecipeImage(uri)
    }
    
    override suspend fun convertLocalUriToByteArray(
        uri: String
    ): Result<ByteArray> {
        return recipeImageLocalDataSource.convertToByteArray(uri)
    }
    
    override suspend fun convertLocalUriToRemoteStorageUri(
        uri: String
    ): Result<String> {
        return recipeImageRemoteDataSource.uploadRecipeImage(uri)
    }
    
    override suspend fun convertByteArrayToLocalStorageUri(
        byteArray: ByteArray,
        fileName: String
    ): Result<String> {
        return recipeImageLocalDataSource.convertToInternalStorageUri(byteArray, fileName)
    }
}
