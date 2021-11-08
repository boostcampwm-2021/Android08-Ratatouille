package com.kdjj.domain.repository

interface RecipeImageRepository {

    suspend fun convertToByteArray(uri: String): Result<ByteArray>

    suspend fun convertToInternalStorageUri(
        byteArray: ByteArray,
        fileName: String
    ): Result<String>
}