package com.kdjj.remote

interface FirebaseStorageDao {

    suspend fun fetchRecipeImage(uri: String): Result<ByteArray>
}