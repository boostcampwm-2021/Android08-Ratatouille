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
    ): Result<List<String>> {
        return imageInfo.chunked(10).map { imgInfoList ->
            recipeImageLocalDataSource.convertToByteArray(imgInfoList.map { it.uri })
                .flatMap { byteArrayDegreePairList ->
                    recipeImageLocalDataSource.convertToInternalStorageUri(
                        byteArrayDegreePairList.map { it.first },
                        imgInfoList.map { it.fileName },
                        byteArrayDegreePairList.map { it.second }
                    )
                }.onFailure {
                    return Result.failure(it)
                }
        }.fold(Result.success(listOf())) { acc, result ->
            acc.flatMap { accList ->
                result.flatMap { resultList ->
                    Result.success(accList + resultList)
                }
            }
        }
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
                }.onFailure {
                    return Result.failure(it)
                }
        }.fold(Result.success(listOf())) { acc, result ->
            acc.flatMap { accList ->
                result.flatMap { resultList ->
                    Result.success(accList + resultList)
                }
            }
        }

    override fun isUriExists(uri: String): Boolean = recipeImageLocalDataSource.isUriExists(uri)

    override suspend fun deleteUselessImages(): Result<Unit> = recipeImageLocalDataSource.deleteUselessImages()
}