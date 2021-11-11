package com.kdjj.local

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import javax.inject.Inject

class FileSaveHelper @Inject constructor(
    private val fileDir: File,
    private val contentResolver: ContentResolver
) {

    suspend fun convertToByteArray(uri: String): Result<ByteArray> = withContext(Dispatchers.IO) {
        try {
            val inputStream = contentResolver.openInputStream(Uri.parse(uri))
            val byteArray = inputStream?.readBytes() ?: throw Exception()
            Result.success(byteArray)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun convertToInternalStorageUri(
        byteArray: ByteArray,
        fileName: String
    ): Result<String> = withContext(Dispatchers.IO) {
        var fos: FileOutputStream? = null
        try {
            val filePath = "$fileDir/${fileName}.png"
            fos = FileOutputStream(filePath)
            val bitmap = convertByteArrayToBitmap(byteArray)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            Result.success(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    private fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val width = bitmap.width.toFloat()
        val height = bitmap.height.toFloat()
        val (scaledWidth, scaledHeight) = if (width > MAX_WIDTH_HEIGHT_SIZE || height > MAX_WIDTH_HEIGHT_SIZE) {
            if (width > height) {
                MAX_WIDTH_HEIGHT_SIZE to (MAX_WIDTH_HEIGHT_SIZE * height / width)
            } else {
                (MAX_WIDTH_HEIGHT_SIZE * width / height) to MAX_WIDTH_HEIGHT_SIZE
            }
        } else {
            width to height
        }
        return Bitmap.createScaledBitmap(bitmap, scaledWidth.toInt(), scaledHeight.toInt(), true)
    }

    companion object{
        const val MAX_WIDTH_HEIGHT_SIZE = 300
    }
}
