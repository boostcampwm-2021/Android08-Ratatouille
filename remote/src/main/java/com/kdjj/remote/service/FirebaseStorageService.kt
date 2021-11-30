package com.kdjj.remote.service

internal interface FirebaseStorageService {
    
    suspend fun fetchRecipeImage(
        uriList: List<String>
    ): List<ByteArray>

    suspend fun uploadRecipeImage(
        uri: String
    ): String
}
