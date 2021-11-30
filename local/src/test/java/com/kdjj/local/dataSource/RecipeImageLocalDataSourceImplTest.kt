package com.kdjj.local.dataSource

import com.kdjj.local.ImageFileHelper
import com.kdjj.local.dao.UselessImageDao
import com.kdjj.local.dto.UselessImageDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class RecipeImageLocalDataSourceImplTest {

    private lateinit var mockImageFileHelper: ImageFileHelper
    private lateinit var mockUselessImageDao: UselessImageDao
    private lateinit var recipeImageLocalDataSourceImpl: RecipeImageLocalDataSourceImpl

    private val testUri = "this is test uri1"

    private val testUriList = listOf(
        "this is test uri1",
        "this is test uri2",
        "this is test uri3",
        "this is test uri4"
    )

    private val testByteArrayWithDegreeList = listOf(
        Pair("this is test uri1".toByteArray(), 90f),
        Pair("this is test uri2".toByteArray(), 90f),
        Pair("this is test uri3".toByteArray(), 90f),
        Pair("this is test uri4".toByteArray(), 90f)
    )

    private val testByteArrayList = listOf(
        "this is test uri1".toByteArray(),
        "this is test uri2".toByteArray(),
        "this is test uri3".toByteArray(),
        "this is test uri4".toByteArray()
    )

    private val testFileNameList = listOf(
        "file1",
        "file2",
        "file3",
        "file4",
    )

    private val testDegreeList = listOf(
        90f,
        90f,
        90f,
        90f,
    )

    private val uselessImageDtoList = listOf(
        UselessImageDto("imagePath"),
        UselessImageDto("imagePath"),
        UselessImageDto("imagePath"),
        UselessImageDto("imagePath")
    )

    @Before
    fun setup() {
        mockUselessImageDao = mock(UselessImageDao::class.java)
        mockImageFileHelper = mock(ImageFileHelper::class.java)
        recipeImageLocalDataSourceImpl =
            RecipeImageLocalDataSourceImpl(mockImageFileHelper, mockUselessImageDao)
    }

    @Test
    fun convertToByteArray_getImageByteArrayAndImageDegreeList_true(): Unit = runBlocking {
        //given
        `when`(mockImageFileHelper.convertToByteArray(testUriList)).thenReturn(
            Result.success(testByteArrayWithDegreeList)
        )

        //when
        val testResult = recipeImageLocalDataSourceImpl.convertToByteArray(testUriList)

        //then
        assertEquals(testByteArrayWithDegreeList, testResult.getOrNull())
    }

    @Test
    fun convertToInternalStorageUri_getInternalStorageUriList_true(): Unit = runBlocking {
        //given
        `when`(
            mockImageFileHelper.convertToInternalStorageUri(
                testByteArrayList,
                testFileNameList,
                testDegreeList
            )
        ).thenReturn(Result.success(testUriList))
        //when
        val testResult = recipeImageLocalDataSourceImpl.convertToInternalStorageUri(
            testByteArrayList,
            testFileNameList,
            testDegreeList
        )
        //then
        assertEquals(testUriList, testResult.getOrNull())
    }

    @Test
    fun isUriExists_getTrue_true(): Unit = runBlocking {
        //given
        `when`(mockImageFileHelper.isUriExists(testUri)).thenReturn(true)
        //when
        val testResult = recipeImageLocalDataSourceImpl.isUriExists(testUri)
        //then
        assertTrue(testResult)
    }

    @Test
    fun isUriExists_getFalse_false(): Unit = runBlocking {
        //given
        `when`(mockImageFileHelper.isUriExists(testUri)).thenReturn(false)
        //when
        val testResult = recipeImageLocalDataSourceImpl.isUriExists(testUri)
        //then
        assertFalse(testResult)
    }

    @Test
    fun deleteUselessImages_getSuccess_true(): Unit = runBlocking {
        //given
        `when`(mockUselessImageDao.getAllUselessImage()).thenReturn(uselessImageDtoList)
        //when
        recipeImageLocalDataSourceImpl.deleteUselessImages()
        //then
        verify(mockImageFileHelper, times(uselessImageDtoList.size))
            .deleteImageFile("imagePath")
        verify(mockUselessImageDao,  times(uselessImageDtoList.size))
            .deleteUselessImage(UselessImageDto("imagePath"))
    }
}
