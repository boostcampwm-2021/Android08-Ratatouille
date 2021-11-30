package com.kdjj.remote.datasource

import com.kdjj.data.datasource.RecipeImageRemoteDataSource
import com.kdjj.remote.service.RecipeImageService
import javax.inject.Inject

internal class RecipeImageRemoteDataSourceImpl @Inject constructor(
    private val recipeImageService: RecipeImageService
) : RecipeImageRemoteDataSource {

    override suspend fun fetchRecipeImage(
        uriList: List<String>
    ): Result<List<ByteArray>> =
        runCatching {
            recipeImageService.fetchRecipeImage(uriList)
        }


    override suspend fun uploadRecipeImage(
        uri: String
    ): Result<String> =
        runCatching {
            recipeImageService.uploadRecipeImage(uri)
        }
}

