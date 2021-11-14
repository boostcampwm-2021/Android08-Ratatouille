package com.kdjj.remote.dao

import android.net.Uri
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

internal class FirebaseStorageDaoImpl @Inject constructor(
    private val storageRef: StorageReference
) : FirebaseStorageDao {
    
    override suspend fun fetchRecipeImage(
        uri: String
    ): Result<ByteArray> {
        return withContext(Dispatchers.IO) {
            try {
                val byteArray = storageRef.storage
                    .getReferenceFromUrl(uri)
                    .getBytes(MAX_SIZE).await()
                Result.success(byteArray)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    override suspend fun uploadRecipeImage(
        uri: String
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val file = Uri.fromFile(File(uri))
                val refer = storageRef.child("images/${file.lastPathSegment}")
                refer.putFile(file).await()
                val newUri = refer.downloadUrl.await()
                Result.success(newUri.toString())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    companion object {
        
        val MAX_SIZE: Long = 10485760L
    }
}
