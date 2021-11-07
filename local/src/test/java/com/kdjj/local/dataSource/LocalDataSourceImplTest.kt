package com.kdjj.local.dataSource

import com.kdjj.domain.model.RecipeStepType
import com.kdjj.local.DAO.RecipeDAO
import com.kdjj.local.FileSaveHelper
import com.kdjj.local.model.RecipeMetaEntity
import com.kdjj.local.model.RecipeStepEntity
import com.kdjj.local.model.RecipeTypeEntity
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.io.FileNotFoundException
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class LocalDataSourceImplTest {

    private lateinit var mockRecipeDAO: RecipeDAO
    private lateinit var mockFileSaveHelper: FileSaveHelper
    private lateinit var localDataSource: LocalDataSourceImpl

    private val testUri = "test Uri"
    private val testByteArray = testUri.toByteArray()
    private val testFileName = "testFileName"
    private val testStorageUri = "test storage uri"

    @Before
    fun setup() {
        //given
        mockRecipeDAO = mock(RecipeDAO::class.java) // Result<Boolean>
        mockFileSaveHelper = mock(FileSaveHelper::class.java)
        localDataSource = LocalDataSourceImpl(mockRecipeDAO, mockFileSaveHelper)
    }

    @Test
    fun insertRecipeMeta_saveRecipeMetaIntoDatabase_true(): Unit = runBlocking {
        //given
        val testRecipeMeta1 = RecipeMetaEntity(
            "recipeID1",
            "title",
            "stuff",
            "image path",
            null,
            false,
            1000,
            1
        )
        //when
        mockRecipeDAO.insertRecipeMeta(testRecipeMeta1)
        //given
        verify(mockRecipeDAO, times(1)).insertRecipeMeta(testRecipeMeta1)
    }

    @Test
    fun insertRecipeType_saveRecipeStepIntoDatabase_true(): Unit = runBlocking {
        //given
        val testRecipeStep = RecipeStepEntity(
            "stepID1",
            "step name1",
            1,
            RecipeStepType.FRY,
            "description1",
            "image path1",
            1000,
            "recipeID1"
        )
        val testRecipeStepList = listOf(testRecipeStep, testRecipeStep)
        //when
        testRecipeStepList.forEach { mockRecipeDAO.insertRecipeStep(it) }
        //then
        verify(mockRecipeDAO, times(2)).insertRecipeStep(testRecipeStep)
    }

    @Test
    fun insertRecipeType(): Unit = runBlocking {
        //given
        val testRecipeType = RecipeTypeEntity(1, "한식")
        //when
        mockRecipeDAO.insertRecipeType(testRecipeType)
        //then
        verify(mockRecipeDAO, times(1)).insertRecipeType(testRecipeType)
    }

    @Test
    fun getAllRecipeTypes_getListOfRecipeType_true(): Unit = runBlocking {
        //given
        val testRecipeTypeList = listOf(
            RecipeTypeEntity(1, "한식"),
            RecipeTypeEntity(2, "일식")
        )
        `when`(mockRecipeDAO.getAllRecipeTypes()).thenReturn(testRecipeTypeList)
        //when
        val recipeTypeList = localDataSource.getRecipeTypes()
        //then
        assertEquals(testRecipeTypeList, recipeTypeList.getOrNull())
    }

    @Test
    fun localUriToByteArray_getImageByteArray_true(): Unit = runBlocking {
        //given
        `when`(mockFileSaveHelper.convertToByteArray(testUri)).thenReturn(Result.success(testByteArray))
        //when
        val imageByteArrayResult = localDataSource.localUriToByteArray(testUri)
        //then
        assertEquals(testByteArray, imageByteArrayResult.getOrNull())
    }

    @Test
    fun localUriToByteArray_occurFileNotFoundException_true(): Unit = runBlocking {
        //given
        given(mockFileSaveHelper.convertToByteArray(testUri)).willAnswer {
            throw FileNotFoundException("File Not Found Exception Occurred")
        }
        try {
            //when
            localDataSource.localUriToByteArray(testUri)
            fail()
        } catch (e: Exception) {
            //then
            assertEquals("File Not Found Exception Occurred", e.message)
        }
    }

    @Test
    fun byteArrayToLocalUri_getImageUri_ImageUri(): Unit = runBlocking {
        //given
        `when`(mockFileSaveHelper.convertToInternalStorageUri(testByteArray, testFileName)).thenReturn(Result.success(testStorageUri))
        //when
        val imageUriResult = localDataSource.byteArrayToLocalUri(testByteArray, testFileName)
        //then
        assertEquals(testStorageUri, imageUriResult.getOrNull())
    }

    @Test
    fun byteArrayToLocalUri_occurIOException_IOException() = runBlocking {
        //given
        given(mockFileSaveHelper.convertToInternalStorageUri(testByteArray, testFileName)
        ).willAnswer {
            throw IOException("IOException Occurred")
        }
        try {
            //when
            localDataSource.byteArrayToLocalUri(testByteArray, testFileName)
            fail()
        } catch (e: Exception) {
            //then
            assertEquals("IOException Occurred", e.message)
        }
    }

    @Test
    fun byteArrayToLocalUri_occurFileNotFoundException_true() = runBlocking {
        //given
        given(
            mockFileSaveHelper.convertToInternalStorageUri(testByteArray, testFileName)
        ).willAnswer {
            throw FileNotFoundException("File Not Found Exception Occurred")
        }
        try {
            //when
            localDataSource.byteArrayToLocalUri(testByteArray, testFileName)
            fail()
        } catch (e: Exception) {
            //then
            assertEquals("File Not Found Exception Occurred", e.message)
        }
    }
}