package com.kdjj.local

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

class FileSaveHelper(private val fileDir: File, private val contentResolver: ContentResolver) {

    suspend fun convertToByteArray(uri: String): Result<ByteArray> {
        return withContext(Dispatchers.IO){
            return@withContext try {
                val inputStream = contentResolver.openInputStream(Uri.parse(uri))
                val byteArray = inputStream?.readBytes()
                // 물어보기
                Result.success(byteArray!!)
            } catch (e: Exception) {
                Result.failure(Exception(e.message))
            }
        }
    }


}
