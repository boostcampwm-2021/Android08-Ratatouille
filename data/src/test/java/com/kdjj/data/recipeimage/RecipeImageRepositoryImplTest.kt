package com.kdjj.data.recipeimage

import com.kdjj.data.datasource.RecipeImageLocalDataSource
import com.kdjj.data.datasource.RecipeImageRemoteDataSource
import com.kdjj.data.repository.RecipeImageRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RecipeImageRepositoryImplTest {

    private lateinit var mockRecipeImageLocalDataSource: RecipeImageLocalDataSource
    private lateinit var mockRecipeImageRemoteDataSource: RecipeImageRemoteDataSource
    private lateinit var recipeImageRepositoryImpl: RecipeImageRepositoryImpl
    private val testUri = "this is test uri"
    private val testByteArray = testUri.toByteArray()
    private val testDegree = 90f
    private val testFileName = "fileName"

    @Before
    fun setup() {
        mockRecipeImageLocalDataSource = mock(RecipeImageLocalDataSource::class.java)
        mockRecipeImageRemoteDataSource = mock(RecipeImageRemoteDataSource::class.java)
        recipeImageRepositoryImpl = RecipeImageRepositoryImpl(
            mockRecipeImageLocalDataSource,
            mockRecipeImageRemoteDataSource
        )
    }

    @Test
    fun copyExternalImageToInternal(): Unit = runBlocking {
        //given
        `when`(mockRecipeImageLocalDataSource.convertToByteArray(testUri)).thenReturn(
            Result.success(testByteArray to testDegree)
        )
        `when`(
            mockRecipeImageLocalDataSource.convertToInternalStorageUri(
                testByteArray,
                testFileName,
                testDegree
            )
        ).thenReturn(Result.success(testUri))

        //when
        val result = recipeImageRepositoryImpl.copyExternalImageToInternal(testUri, testFileName)

        //then
        assertEquals(testUri, result.getOrNull())
    }

    @Test
    fun copyRemoteImageToInternal(): Unit = runBlocking {
        //givne
        `when`(mockRecipeImageRemoteDataSource.fetchRecipeImage(testUri)).thenReturn(
            Result.success(testByteArray)
        )
        `when`(
            mockRecipeImageLocalDataSource.convertToInternalStorageUri(
                testByteArray,
                testFileName
            )
        ).thenReturn(Result.success(testUri))

        //when
        val result = recipeImageRepositoryImpl.copyRemoteImageToInternal(testUri, testFileName)

        //then
        assertEquals(testUri, result.getOrNull())
    }

    @Test
    fun convertToRemoteStorageUri_getRemoteImageUri_true(): Unit = runBlocking {
        //given
        `when`(mockRecipeImageRemoteDataSource.uploadRecipeImage(testUri)).thenReturn(
            Result.success(testUri)
        )
        //when
        val newImagePathResult =
            recipeImageRepositoryImpl.convertInternalUriToRemoteStorageUri(testUri)
        //then
        assertEquals(testUri, newImagePathResult.getOrNull())
    }
}
