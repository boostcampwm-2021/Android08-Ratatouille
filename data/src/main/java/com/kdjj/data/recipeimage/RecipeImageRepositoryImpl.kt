package com.kdjj.data.recipeimage

import com.kdjj.domain.repository.RecipeImageRepository
import javax.inject.Inject

class RecipeImageRepositoryImpl @Inject constructor(
    private val recipeImageLocalDataSource: RecipeImageLocalDataSource
): RecipeImageRepository {

    override suspend fun convertToByteArray(uri: String): Result<ByteArray> {
        return recipeImageLocalDataSource.convertToByteArray(uri)
    }

    override suspend fun convertToInternalStorageUri(
        byteArray: ByteArray,
        fileName: String
    ): Result<String> {
        return recipeImageLocalDataSource.convertToInternalStorageUri(byteArray, fileName)
    }
}