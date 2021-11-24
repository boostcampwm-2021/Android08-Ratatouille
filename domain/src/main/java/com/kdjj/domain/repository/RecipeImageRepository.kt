package com.kdjj.domain.repository

interface RecipeImageRepository {

    suspend fun convertInternalUriToRemoteStorageUri(
        uri: String
    ): Result<String>

    suspend fun copyExternalImageToInternal(
        uri: String,
        fileName: String
    ): Result<String>

    suspend fun copyRemoteImageToInternal(
        uri: String,
        fileName: String
    ): Result<String>

    fun isUriExists(
        uri: String
    ): Boolean
}
