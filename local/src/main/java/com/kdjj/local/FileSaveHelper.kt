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
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fos)
            Result.success(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    private fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap{
        val options = BitmapFactory.Options()
        options.inSampleSize = 4
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        val width = 300
        val height = 300

        var bmpWidth = bitmap.width.toFloat()
        var bmpHeight = bitmap.height.toFloat()

        if (bmpWidth > width) {
            val scale = width / (bmpWidth / 100)
            bmpWidth *= (scale / 100)
            bmpHeight *= (scale / 100)
        } else if(bmpHeight > height){
            val scale = width / (bmpHeight / 100)
            bmpWidth *= (scale / 100)
            bmpHeight *= (scale / 100)
        }
        return Bitmap.createScaledBitmap(bitmap, bmpWidth.toInt(), bmpHeight.toInt(), true)
    }
}
