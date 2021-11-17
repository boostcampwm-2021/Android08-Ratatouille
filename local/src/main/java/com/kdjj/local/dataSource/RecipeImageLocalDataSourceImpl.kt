package com.kdjj.local.dataSource

import com.kdjj.data.datasource.RecipeImageLocalDataSource
import com.kdjj.local.FileSaveHelper
import javax.inject.Inject

internal class RecipeImageLocalDataSourceImpl @Inject constructor(
    private val fileSaveHelper: FileSaveHelper
) : RecipeImageLocalDataSource {

    override suspend fun convertToByteArray(
        uri: String
    ): Result<Pair<ByteArray, Float?>> {
        return fileSaveHelper.convertToByteArray(uri)
    }

    override suspend fun convertToInternalStorageUri(
        byteArray: ByteArray,
        fileName: String,
        degree: Float?
    ): Result<String> {
        return fileSaveHelper.convertToInternalStorageUri(byteArray, fileName, degree)
    }

    override suspend fun copyExif(
        oldPath: String,
        newPath: String
    ): Result<Unit> {
        return fileSaveHelper.copyExif(oldPath, newPath)
    }
}

