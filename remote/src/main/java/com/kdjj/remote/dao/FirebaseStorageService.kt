package com.kdjj.remote.dao

internal interface FirebaseStorageService {
    
    suspend fun fetchRecipeImage(
        uri: String
    ): Result<ByteArray>
    
    suspend fun uploadRecipeImage(
        uri: String
    ): Result<String>
}
