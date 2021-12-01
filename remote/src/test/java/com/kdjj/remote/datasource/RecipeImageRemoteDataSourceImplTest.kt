package com.kdjj.remote.datasource

import com.kdjj.remote.service.RecipeImageService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RecipeImageRemoteDataSourceImplTest {

    private lateinit var mockRecipeImageService: RecipeImageService
    private lateinit var mockRecipeImageRemoteDataSourceImpl: RecipeImageRemoteDataSourceImpl
    private val dummyUriList = listOf(
        "dummyUri1",
        "dummyUri2",
        "dummyUri3"
    )
    private val dummyByteArray = dummyUriList.map { it.toByteArray() }
    private val dummyUploadRecipeImageUri = "dummyUploadRecipeImage"

    @Before
    fun setup() {
        // given
        mockRecipeImageService = mock(RecipeImageService::class.java)
        mockRecipeImageRemoteDataSourceImpl = RecipeImageRemoteDataSourceImpl(mockRecipeImageService)
    }

    @Test
    fun fetchRecipeImage_giveUrlList_successAndByteArray(): Unit = runBlocking {
        // given
        `when`(mockRecipeImageService.fetchRecipeImage(dummyUriList)).thenReturn(dummyByteArray)

        // when
        val fetchRecipeImageResult = mockRecipeImageRemoteDataSourceImpl.fetchRecipeImage(dummyUriList)

        // then
        assertTrue(fetchRecipeImageResult.isSuccess)
        assertEquals(dummyByteArray, fetchRecipeImageResult.getOrNull())
    }

    @Test
    fun uploadRecipeImage_giveUri_successAndUri(): Unit = runBlocking {
        // given
        `when`(mockRecipeImageService.uploadRecipeImage(dummyUriList.first())).thenReturn(dummyUploadRecipeImageUri)

        // when
        val uploadRecipeImageResult = mockRecipeImageRemoteDataSourceImpl.uploadRecipeImage(dummyUriList.first())

        // then
        assertTrue(uploadRecipeImageResult.isSuccess)
        assertEquals(dummyUploadRecipeImageUri, uploadRecipeImageResult.getOrNull())
    }
}