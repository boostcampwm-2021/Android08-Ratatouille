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

    private val dummyUri = "this is test uri1"

    private val dummyUriList = listOf(
        "this is test uri1",
        "this is test uri2",
        "this is test uri3",
        "this is test uri4"
    )

    private val dummyByteArrayList = listOf(
        "this is test uri1".toByteArray(),
        "this is test uri2".toByteArray(),
        "this is test uri3".toByteArray(),
        "this is test uri4".toByteArray()
    )

    private val dummyFileNameList = listOf(
        "file1",
        "file2",
        "file3",
        "file4",
    )

    private val dummyDegreeList = listOf(
        90f,
        90f,
        90f,
        90f,
    )

    private val dummyUselessImageDtoList = listOf(
        UselessImageDto("imagePath"),
        UselessImageDto("imagePath"),
        UselessImageDto("imagePath"),
        UselessImageDto("imagePath")
    )

    private val dummyByteArrayWithDegreeList = dummyByteArrayList.zip(dummyDegreeList)

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
        `when`(mockImageFileHelper.convertToByteArray(dummyUriList)).thenReturn(
            Result.success(dummyByteArrayWithDegreeList)
        )
        //when
        val testResult = recipeImageLocalDataSourceImpl.convertToByteArray(dummyUriList)
        //then
        assertEquals(dummyByteArrayWithDegreeList, testResult.getOrNull())
    }

    @Test
    fun convertToInternalStorageUri_getInternalStorageUriList_true(): Unit = runBlocking {
        //given
        `when`(
            mockImageFileHelper.convertToInternalStorageUri(
                dummyByteArrayList,
                dummyFileNameList,
                dummyDegreeList
            )
        ).thenReturn(Result.success(dummyUriList))
        //when
        val testResult = recipeImageLocalDataSourceImpl.convertToInternalStorageUri(
            dummyByteArrayList,
            dummyFileNameList,
            dummyDegreeList
        )
        //then
        assertEquals(dummyUriList, testResult.getOrNull())
    }

    @Test
    fun isUriExists_getTrue_true(): Unit = runBlocking {
        //given
        `when`(mockImageFileHelper.isUriExists(dummyUri)).thenReturn(true)
        //when
        val testResult = recipeImageLocalDataSourceImpl.isUriExists(dummyUri)
        //then
        assertTrue(testResult)
    }

    @Test
    fun isUriExists_getFalse_false(): Unit = runBlocking {
        //given
        `when`(mockImageFileHelper.isUriExists(dummyUri)).thenReturn(false)
        //when
        val testResult = recipeImageLocalDataSourceImpl.isUriExists(dummyUri)
        //then
        assertFalse(testResult)
    }

    @Test
    fun deleteUselessImages_getSuccess_true(): Unit = runBlocking {
        //given
        `when`(mockUselessImageDao.getAllUselessImage()).thenReturn(dummyUselessImageDtoList)
        //when
        recipeImageLocalDataSourceImpl.deleteUselessImages()
        //then
        verify(mockImageFileHelper, times(dummyUselessImageDtoList.size))
            .deleteImageFile("imagePath")
        verify(mockUselessImageDao, times(dummyUselessImageDtoList.size))
            .deleteUselessImage(UselessImageDto("imagePath"))
    }
}
