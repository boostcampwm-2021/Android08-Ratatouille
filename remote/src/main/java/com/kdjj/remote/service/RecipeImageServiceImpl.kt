package com.kdjj.remote.service

import android.net.Uri
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

internal class RecipeImageServiceImpl @Inject constructor(
    private val storageRef: StorageReference
) : RecipeImageService {

    override suspend fun fetchRecipeImage(
        uriList: List<String>
    ): List<ByteArray> =
        withContext(Dispatchers.IO) {
            val res = uriList.map {
                async {
                    storageRef.storage
                        .getReferenceFromUrl(it)
                        .getBytes(MAX_SIZE)
                        .await()
                }
            }
            res.map { it.await() }
        }


    override suspend fun uploadRecipeImage(
        uri: String
    ): String =
        withContext(Dispatchers.IO) {
            val file = Uri.fromFile(File(uri))
            val refer = storageRef.child("images/${file.lastPathSegment}")
            refer.putFile(file).await()
            val newUri = refer.downloadUrl.await()
            newUri.toString()
        }

    companion object {

        val MAX_SIZE: Long = 10485760L
    }
}
