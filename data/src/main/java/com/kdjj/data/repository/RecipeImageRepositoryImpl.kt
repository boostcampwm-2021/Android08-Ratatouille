package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeImageLocalDataSource
import com.kdjj.data.datasource.RecipeImageRemoteDataSource
import com.kdjj.domain.repository.RecipeImageRepository
import javax.inject.Inject

internal class RecipeImageRepositoryImpl @Inject constructor(
    private val recipeImageLocalDataSource: RecipeImageLocalDataSource,
    private val recipeImageRemoteDataSource: RecipeImageRemoteDataSource
) : RecipeImageRepository {

    override suspend fun convertInternalUriToRemoteStorageUri(
        uri: String
    ): Result<String> {
        return recipeImageRemoteDataSource.uploadRecipeImage(uri)
    }

    override suspend fun copyExternalImageToInternal(
        uri: String,
        fileName: String
    ): Result<String> = runCatching {
        val (byteArray, degree) = recipeImageLocalDataSource.convertToByteArray(uri)
            .getOrThrow()
        val newUri = recipeImageLocalDataSource.convertToInternalStorageUri(byteArray, fileName, degree)
            .getOrThrow()
        newUri
    }

    override suspend fun copyRemoteImageToInternal(
        uri: String,
        fileName: String
    ): Result<String> = runCatching {
        val byteArray = recipeImageRemoteDataSource.fetchRecipeImage(uri)
            .getOrThrow()
        val newUri = recipeImageLocalDataSource.convertToInternalStorageUri(byteArray, fileName)
            .getOrThrow()
        recipeImageLocalDataSource.copyExif(uri, newUri)
            .getOrThrow()
        newUri
    }
}