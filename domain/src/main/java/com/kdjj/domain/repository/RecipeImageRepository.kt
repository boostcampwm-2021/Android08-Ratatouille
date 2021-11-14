package com.kdjj.domain.repository

interface RecipeImageRepository {
    
    suspend fun convertRemoteUriToByteArray(
        uri: String
    ): Result<ByteArray>
    
    suspend fun convertLocalUriToByteArray(
        uri: String
    ): Result<ByteArray>
    
    suspend fun convertLocalUriToRemoteStorageUri(
        uri: String
    ): Result<String>
    
    suspend fun convertByteArrayToLocalStorageUri(
        byteArray: ByteArray,
        fileName: String
    ): Result<String>
}
