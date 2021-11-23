package com.kdjj.local.dataSource

import com.kdjj.data.datasource.RecipeImageLocalDataSource
import com.kdjj.local.FileSaveHelper
import com.kdjj.local.dao.ImageValidationDao
import com.kdjj.local.dto.ImageValidation
import javax.inject.Inject

internal class RecipeImageLocalDataSourceImpl @Inject constructor(
    private val fileSaveHelper: FileSaveHelper,
    private val imageValidationDao: ImageValidationDao
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
        imageValidationDao.insertImageValidation(ImageValidation(fileName, false))
        return fileSaveHelper.convertToInternalStorageUri(byteArray, fileName, degree)
    }
}

