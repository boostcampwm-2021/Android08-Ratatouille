package com.kdjj.data.repository

import com.kdjj.domain.common.flatMap
import com.kdjj.data.datasource.RecipeImageLocalDataSource
import com.kdjj.data.datasource.RecipeImageRemoteDataSource
import com.kdjj.domain.model.ImageInfo
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
        imageInfo: List<ImageInfo>
    ): Result<List<String>> =
        imageInfo.chunked(10).map { imgInfoList ->
            recipeImageLocalDataSource.convertToByteArray(imgInfoList.map { it.uri })
                .flatMap { byteArrayDegreePairList ->
                    recipeImageLocalDataSource.convertToInternalStorageUri(
                        byteArrayDegreePairList.map { it.first },
                        imgInfoList.map {it.fileName},
                        byteArrayDegreePairList.map { it.second }
                    )
                }

        }.fold(Result.success(emptyList())) { total, item ->
            Result.success(
                total.getOrThrow()
                    .plus(item.getOrThrow())
            )
        }

    override suspend fun copyRemoteImageToInternal(
        imageInfo: List<ImageInfo>
    ): Result<List<String>> =
        imageInfo.chunked(10).map { imgInfoList ->
            recipeImageRemoteDataSource.fetchRecipeImage(imgInfoList.map { it.uri })
                .flatMap { byteArrayList ->
                    recipeImageLocalDataSource.convertToInternalStorageUri(
                        byteArrayList,
                        imgInfoList.map { it.fileName },
                        (0..byteArrayList.size).map { null }
                    )
                }
        }.fold(Result.success(emptyList())) { total, item ->
            Result.success(
                total.getOrThrow()
                    .plus(item.getOrThrow())
            )
        }



    override fun isUriExists(uri: String): Boolean = recipeImageLocalDataSource.isUriExists(uri)

    override suspend fun deleteUselessImages(): Result<Unit> = recipeImageLocalDataSource.deleteUselessImages()
}