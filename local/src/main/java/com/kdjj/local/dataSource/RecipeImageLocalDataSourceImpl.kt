package com.kdjj.local.dataSource

import com.kdjj.data.datasource.RecipeImageLocalDataSource
import com.kdjj.local.ImageFileHelper
import com.kdjj.local.dao.UselessImageDao
import com.kdjj.local.dto.UselessImageDto
import javax.inject.Inject

internal class RecipeImageLocalDataSourceImpl @Inject constructor(
    private val imageFileHelper: ImageFileHelper,
    private val uselessImageDao: UselessImageDao
) : RecipeImageLocalDataSource {

    override suspend fun convertToByteArray(
        uri: String
    ): Result<Pair<ByteArray, Float?>> {
        return imageFileHelper.convertToByteArray(uri)
    }

    override suspend fun convertToInternalStorageUri(
        byteArray: ByteArray,
        fileName: String,
        degree: Float?
    ): Result<String> {
        return imageFileHelper.convertToInternalStorageUri(byteArray, fileName, degree)
    }

    override fun isUriExists(
        uri: String
    ): Boolean = imageFileHelper.isUriExists(uri)

    override suspend fun deleteUselessImages(): Result<Unit> =
        runCatching {
            uselessImageDao.getAllUselessImage()
                .forEach {
                    imageFileHelper.deleteImageFile(it.imgPath)
                    uselessImageDao.deleteUselessImage(UselessImageDto(it.imgPath))
                }
        }
}

