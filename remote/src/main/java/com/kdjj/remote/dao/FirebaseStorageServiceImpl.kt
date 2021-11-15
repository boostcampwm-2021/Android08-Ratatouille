package com.kdjj.remote.dao

import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.kdjj.data.common.errorMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

internal class FirebaseStorageServiceImpl @Inject constructor(
    private val storageRef: StorageReference
) : FirebaseStorageService {
    
    override suspend fun fetchRecipeImage(
        uri: String
    ): Result<ByteArray> {
        return withContext(Dispatchers.IO) {
            runCatching {
                storageRef.storage
                    .getReferenceFromUrl(uri)
                    .getBytes(MAX_SIZE)
                    .await()
            }.errorMap {
                Exception(it.message)
            }
        }
    }
    
    override suspend fun uploadRecipeImage(
        uri: String
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val file = Uri.fromFile(File(uri))
                val refer = storageRef.child("images/${file.lastPathSegment}")
                refer.putFile(file).await()
                val newUri = refer.downloadUrl.await()
                newUri.toString()
            }.errorMap {
                Exception(it.message)
            }
        }
    }
    
    companion object {
        
        val MAX_SIZE: Long = 10485760L
    }
}
