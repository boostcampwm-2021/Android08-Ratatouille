package com.kdjj.remote

import com.google.firebase.storage.FirebaseStorage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class FirebaseStorageDaoImpl @Inject constructor(
    private val storage: FirebaseStorage
) : FirebaseStorageDao {

    override suspend fun fetchRecipeImage(uri: String): Result<ByteArray> {
        val maxSize: Long = 3238 * 3238
        return withContext(Dispatchers.IO) {
            try {
                val byteArray = storage.getReferenceFromUrl(uri).getBytes(maxSize).await()
                Result.success(byteArray)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}