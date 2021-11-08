package com.kdjj.remote

interface FirebaseStorageDao {

    suspend fun FetchRecipeImage(uri: String):  Result< ByteArray>
}