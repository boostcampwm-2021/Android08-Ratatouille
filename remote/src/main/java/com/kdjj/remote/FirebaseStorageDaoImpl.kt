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
        val tenMegaByte: Long = 10485760L
        return withContext(Dispatchers.IO) {
            try {
                val byteArray = storage.getReferenceFromUrl(uri).getBytes(tenMegaByte).await()
                Result.success(byteArray)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}