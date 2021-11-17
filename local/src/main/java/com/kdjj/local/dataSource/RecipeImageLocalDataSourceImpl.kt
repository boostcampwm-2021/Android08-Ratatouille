package com.kdjj.local.dataSource

import android.media.ExifInterface
import com.kdjj.data.common.errorMap
import com.kdjj.data.datasource.RecipeImageLocalDataSource
import com.kdjj.local.FileSaveHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

internal class RecipeImageLocalDataSourceImpl @Inject constructor(
    private val fileSaveHelper: FileSaveHelper
) : RecipeImageLocalDataSource {

    override suspend fun convertToByteArray(
        uri: String
    ): Result<ByteArray> {
        return fileSaveHelper.convertToByteArray(uri)
    }

    override suspend fun convertToInternalStorageUri(
        byteArray: ByteArray,
        fileName: String
    ): Result<String> {
        return fileSaveHelper.convertToInternalStorageUri(byteArray, fileName)
    }

    override suspend fun copyExif(
        oldPath: String,
        newPath: String
    ): Result<Unit> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val oldExif = ExifInterface(oldPath)
                val exifOrientation = oldExif.getAttribute(ExifInterface.TAG_ORIENTATION)
                exifOrientation?.let {
                    val newExif = ExifInterface(newPath)
                    newExif.setAttribute(ExifInterface.TAG_ORIENTATION, exifOrientation)
                    newExif.saveAttributes()
                }
                return@runCatching
            }.errorMap {
                Exception(it.message)
            }
        }
}

