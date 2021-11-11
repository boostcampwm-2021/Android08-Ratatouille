package com.kdjj.remote

internal interface FirebaseStorageDao {

    suspend fun fetchRecipeImage(uri: String): Result<ByteArray>

    suspend fun uploadRecipeImage(uri: String): Result<String>
}
