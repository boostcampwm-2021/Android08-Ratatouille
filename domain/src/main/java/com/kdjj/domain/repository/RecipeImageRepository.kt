package com.kdjj.domain.repository

import com.kdjj.domain.model.ImageInfo

interface RecipeImageRepository {

    suspend fun convertInternalUriToRemoteStorageUri(
        uri: String
    ): Result<String>

    suspend fun copyExternalImageToInternal(
        imageInfo: List<ImageInfo>
    ): Result<List<String>>

    suspend fun copyRemoteImageToInternal(
        imageInfo: List<ImageInfo>
    ): Result<List<String>>

    fun isUriExists(
        uri: String
    ): Boolean

    suspend fun deleteUselessImages(): Result<Unit>
}
