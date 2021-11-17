package com.kdjj.local

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import android.util.Log
import com.kdjj.data.common.errorMap
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

    suspend fun convertToByteArray(uri: String): Result<Pair<ByteArray, Float?>> = withContext(Dispatchers.IO) {
        runCatching {
            val inputStream = contentResolver.openInputStream(Uri.parse(uri))
            val byteArray = inputStream?.readBytes() ?: throw Exception()

            val exifInputStream = contentResolver.openInputStream(Uri.parse(uri))
            val oldExif = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ExifInterface(exifInputStream ?: throw Exception())
            } else {
                ExifInterface(uri)
            }
            val degree = when (oldExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> null
            }
            byteArray to degree
        }.errorMap {
            Exception(it.message)
        }
    }

    suspend fun convertToInternalStorageUri(
        byteArray: ByteArray,
        fileName: String,
        degree: Float?
    ): Result<String> = withContext(Dispatchers.IO) {
        var fos: FileOutputStream? = null
        runCatching {
            val filePath = "$fileDir/${fileName}.png"
            fos = FileOutputStream(filePath)
            val bitmap = convertByteArrayToBitmap(byteArray, degree)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            filePath
        }.errorMap {
            Exception(it.message)
        }
    }

    private fun convertByteArrayToBitmap(byteArray: ByteArray, degree: Float?): Bitmap {
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
        return Bitmap.createScaledBitmap(bitmap, scaledWidth.toInt(), scaledHeight.toInt(), true).let { scaledBitmap ->
            degree?.let {
                val matrix = Matrix().apply {
                    postRotate(degree)
                }
                Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
            } ?: scaledBitmap
        }
    }

    suspend fun copyExif(
        oldPath: String,
        newPath: String
    ): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val inputStream = contentResolver.openInputStream(Uri.parse(oldPath))
                val oldExif = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ExifInterface(inputStream ?: throw Exception())
                } else {
                    ExifInterface(oldPath)
                }
                val exifOrientation = oldExif.getAttribute(ExifInterface.TAG_ORIENTATION)
                exifOrientation?.let {
                    val newExif = ExifInterface(newPath)
                    newExif.setAttribute(ExifInterface.TAG_ORIENTATION, exifOrientation)
                    newExif.saveAttributes()
                }
                return@runCatching
            }
        }

    companion object{
        const val MAX_WIDTH_HEIGHT_SIZE = 300
    }
}
