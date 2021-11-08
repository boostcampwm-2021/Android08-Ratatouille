package com.kdjj.domain.repository

interface RecipeImageRepository {

    suspend fun convertToByteArrayRemote(uri: String): Result<ByteArray>

    suspend fun convertToByteArrayLocal(uri: String): Result<ByteArray>

    suspend fun convertToInternalStorageUri(
        byteArray: ByteArray,
        fileName: String
    ): Result<String>
}