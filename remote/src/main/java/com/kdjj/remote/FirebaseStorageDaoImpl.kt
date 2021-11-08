package com.kdjj.remote

import android.net.Uri
import com.google.firebase.storage.StorageReference

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class FirebaseStorageDaoImpl @Inject constructor(
    private val storageRef: StorageReference
) : FirebaseStorageDao {

    override suspend fun fetchRecipeImage(uri: String): Result<ByteArray> {
        val tenMegaByte: Long = 10485760L
        return withContext(Dispatchers.IO) {
            try {
                val byteArray = storageRef.storage.getReferenceFromUrl(uri).getBytes(tenMegaByte).await()
                Result.success(byteArray)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun uploadRecipeImage(uri: String): Result<String>{
        return withContext(Dispatchers.IO){
            try {
                val file = Uri.fromFile(File(uri))
                val refer = storageRef.child("images/${file.lastPathSegment}")
                refer.putFile(file).await()
                val newUri = refer.downloadUrl.await()
                Result.success(newUri.toString())
            } catch (e: Exception){
                Result.failure(e)
            }
        }
    }
}