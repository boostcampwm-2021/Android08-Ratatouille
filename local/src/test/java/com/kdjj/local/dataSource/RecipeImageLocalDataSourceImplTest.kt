package com.kdjj.local.dataSource

import com.kdjj.local.FileSaveHelper
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RecipeImageLocalDataSourceImplTest {

    lateinit var mockFileSaveHelper: FileSaveHelper
    lateinit var recipeImageLocalDataSourceImpl: RecipeImageLocalDataSourceImpl

    val testUri = "this is test uri"
    val testByteArray = testUri.toByteArray()

    @Before
    fun setup(){
        mockFileSaveHelper = mock(FileSaveHelper::class.java)
        recipeImageLocalDataSourceImpl = RecipeImageLocalDataSourceImpl(mockFileSaveHelper)
    }

    @Test
    fun convertToByteArray_getImageByteArray_true(): Unit = runBlocking {
        //given
        `when`(recipeImageLocalDataSourceImpl.convertToByteArray(testUri)).thenReturn(Result.success(testByteArray))
        //when
        val byteArrayResult = recipeImageLocalDataSourceImpl.convertToByteArray(testUri)
        //then
        assertEquals(testByteArray, byteArrayResult.getOrNull())
    }

    @Test
    fun convertToInternalStorageUri_localStorageImageUri_true(): Unit = runBlocking {
        //given
        `when`(recipeImageLocalDataSourceImpl.convertToInternalStorageUri(testByteArray, "fileName")).thenReturn(Result.success(testUri))
        //when
        val newLocalImagePathResult = recipeImageLocalDataSourceImpl.convertToInternalStorageUri(testByteArray, "fileName")
        //then
        assertEquals(testUri, newLocalImagePathResult.getOrNull())
    }
}