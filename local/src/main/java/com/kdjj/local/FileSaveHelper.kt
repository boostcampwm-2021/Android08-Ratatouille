package com.kdjj.local

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import com.kdjj.local.dao.ImageValidationDao
import com.kdjj.local.dto.ImageValidationDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import javax.inject.Inject

internal class FileSaveHelper @Inject constructor(
    private val fileDir: File,
    private val contentResolver: ContentResolver,
    private val imageValidationDao: ImageValidationDao
) {

    suspend fun convertToByteArray(uri: String): Result<Pair<ByteArray, Float?>> = withContext(Dispatchers.IO) {
        runCatching {
            val changedUri = if (!uri.contains("://")) "file://${uri}" else uri
            val inputStream = contentResolver.openInputStream(Uri.parse(changedUri))
            val byteArray = inputStream?.readBytes() ?: throw Exception()

            val exifInputStream = contentResolver.openInputStream(Uri.parse(changedUri))
            val oldExif = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ExifInterface(exifInputStream ?: throw Exception())
            } else {
                ExifInterface(changedUri)
            }
            val degree = when (oldExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> null
            }
            byteArray to degree
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
            imageValidationDao.insertImageValidation(ImageValidationDto(filePath, false))
            fos = FileOutputStream(filePath)
            val bitmap = convertByteArrayToBitmap(byteArray, degree)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            filePath
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

    companion object{
        const val MAX_WIDTH_HEIGHT_SIZE = 300
    }
}
