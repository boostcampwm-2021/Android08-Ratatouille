package com.kdjj.remote

interface FirebaseStorageDao {

    suspend fun FetchRecipeImage(): ByteArray
}