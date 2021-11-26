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
        uriList: List<String>
    ): Result<List<Pair<ByteArray, Float?>>> {
        return imageFileHelper.convertToByteArray(uriList)
    }

    override suspend fun convertToInternalStorageUri(
        byteArrayList: List<ByteArray>,
        fileNameList: List<String>,
        degreeList: List<Float?>
    ): Result<List<String>> {
        return imageFileHelper.convertToInternalStorageUri(byteArrayList, fileNameList, degreeList)
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

