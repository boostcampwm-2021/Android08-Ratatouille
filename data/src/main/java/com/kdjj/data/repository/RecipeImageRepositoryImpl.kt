package com.kdjj.data.repository

import com.kdjj.domain.common.flatMap
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
    ): Result<String> = recipeImageLocalDataSource.convertToByteArray(uri)
        .flatMap { (byteArray, degree) ->
            recipeImageLocalDataSource.convertToInternalStorageUri(byteArray, fileName, degree)
        }

    override suspend fun copyRemoteImageToInternal(
        uri: String,
        fileName: String
    ): Result<String> = recipeImageRemoteDataSource.fetchRecipeImage(uri)
        .flatMap { byteArray ->
            recipeImageLocalDataSource.convertToInternalStorageUri(byteArray, fileName)
        }

    override fun isUriExists(uri: String): Boolean = recipeImageLocalDataSource.isUriExists(uri)

    override suspend fun deleteUselessImages(): Result<Unit> = recipeImageLocalDataSource.deleteUselessImages()
}