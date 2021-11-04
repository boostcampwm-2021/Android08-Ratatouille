package com.kdjj.local

import android.content.ContentResolver
import android.net.Uri
import android.os.FileUtils
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class FileSaveHelper(private val fileDir: File, private val contentResolver: ContentResolver) {

    suspend fun convertToByteArray(uri: String): Result<ByteArray> {
        return withContext(Dispatchers.IO) {
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

    suspend fun convertToInternalStorageUri(
        byteArray: ByteArray,
        fileName: String
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            val filePath = "$fileDir/$fileName"
            var fos: FileOutputStream? = null
            return@withContext try {
                fos = FileOutputStream(filePath)
                fos.write(byteArray)
                Result.success(filePath)
            } catch (e: Exception) {
                Result.failure(Exception(e))
            }
        }
    }
}
