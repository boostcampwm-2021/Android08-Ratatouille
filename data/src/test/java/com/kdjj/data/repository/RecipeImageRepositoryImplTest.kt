package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeImageLocalDataSource
import com.kdjj.data.datasource.RecipeImageRemoteDataSource
import com.kdjj.domain.model.ImageInfo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class RecipeImageRepositoryImplTest {
    private lateinit var repositoryImpl: RecipeImageRepositoryImpl
    private lateinit var localDataSource: RecipeImageLocalDataSource
    private lateinit var remoteDataSource: RecipeImageRemoteDataSource

    @Before
    fun setup() {
        localDataSource = mock(RecipeImageLocalDataSource::class.java)
        remoteDataSource = mock(RecipeImageRemoteDataSource::class.java)
        repositoryImpl = RecipeImageRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun convertInternalUriToRemoteStorageUri_givenSuccess_returnsSuccess() {
        runBlocking {
            val testUri = "testUri"
            val successUri = "successUri"
            // given
            `when`(remoteDataSource.uploadRecipeImage(testUri))
                .thenReturn(Result.success(successUri))

            // when
            val result = remoteDataSource.uploadRecipeImage(testUri)

            // then
            assertTrue(result.isSuccess)
            assertEquals(successUri, result.getOrNull())
        }
    }

    @Test
    fun convertInternalUriToRemoteStorageUri_givenFailure_returnsFailure() {
        runBlocking {
            val testUri = "testUri"
            // given
            `when`(remoteDataSource.uploadRecipeImage(testUri))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = remoteDataSource.uploadRecipeImage(testUri)

            // then
            assertTrue(result.isFailure)
        }
    }

    @Test
    fun copyExternalImageToInternal_allSuccess_returnsSuccess() {
        runBlocking {
            // given
            val imageInfo = (1..40).map { ImageInfo("uri${it}", "fileName${it}") }

            `when`(localDataSource.convertToByteArray(anyList()))
                .thenReturn(Result.success((1..10).map { byteArrayOf() to 0f }))

            `when`(localDataSource.convertToInternalStorageUri(anyList(), anyList(), anyList()))
                .thenReturn(Result.success((1..10).map { "newUri${it}" }))

            // when
            val result = repositoryImpl.copyExternalImageToInternal(imageInfo)

            // then
            assertTrue(result.isSuccess)
            assertEquals(40, result.getOrNull()?.size)
        }
    }

    @Test
    fun copyExternalImageToInternal_secondToByteArrayFailure_calledTwice() {
        runBlocking {
            // given
            val imageInfo = (1..40).map { ImageInfo("uri${it}", "fileName${it}") }

            (1..40 step 10).forEach { start ->
                if (start == 11) {
                    `when`(localDataSource.convertToByteArray((start..start+9).map { "uri${it}" }))
                        .thenReturn(Result.failure(Exception()))
                } else {
                    `when`(localDataSource.convertToByteArray((start..start+9).map { "uri${it}" }))
                        .thenReturn(Result.success((start..start+9).map { byteArrayOf(it.toByte()) to 0f }))
                }
            }

            // when
            val result = repositoryImpl.copyExternalImageToInternal(imageInfo)

            // then
            assertTrue(result.isFailure)
            verify(localDataSource, times(2)).convertToByteArray(anyList())
            verify(localDataSource, times(1)).convertToInternalStorageUri(anyList(), anyList(), anyList())
        }
    }

    @Test
    fun copyRemoteImageToInternal_allSuccess_returnsSuccess() {
        runBlocking {
            // given
            val imageInfo = (1..40).map { ImageInfo("uri${it}", "fileName${it}") }

            `when`(remoteDataSource.fetchRecipeImage(anyList()))
                .thenReturn(Result.success((1..10).map { byteArrayOf() }))

            `when`(localDataSource.convertToInternalStorageUri(anyList(), anyList(), anyList()))
                .thenReturn(Result.success((1..10).map { "newUri${it}" }))

            // when
            val result = repositoryImpl.copyRemoteImageToInternal(imageInfo)

            // then
            assertTrue(result.isSuccess)
            assertEquals(40, result.getOrNull()?.size)
        }
    }

    @Test
    fun copyRemoteImageToInternal_secondFetchFailure_calledTwice() {
        runBlocking {
            // given
            val imageInfo = (1..40).map { ImageInfo("uri${it}", "fileName${it}") }

            (1..40 step 10).forEach { start ->
                if (start == 11) {
                    `when`(remoteDataSource.fetchRecipeImage((start..start+9).map { "uri${it}" }))
                        .thenReturn(Result.failure(Exception()))
                } else {
                    `when`(remoteDataSource.fetchRecipeImage((start..start+9).map { "uri${it}" }))
                        .thenReturn(Result.success((start..start+9).map { byteArrayOf(it.toByte()) }))
                }
            }

            // when
            val result = repositoryImpl.copyRemoteImageToInternal(imageInfo)

            // then
            assertTrue(result.isFailure)
            verify(remoteDataSource, times(2)).fetchRecipeImage(anyList())
            verify(localDataSource, times(1)).convertToInternalStorageUri(anyList(), anyList(), anyList())
        }
    }

    @Test
    fun isUriExists_givenFalse_returnsFalse() {
        // given
        val testUri = "testUri"
        `when`(localDataSource.isUriExists(testUri)).thenReturn(false)

        // when
        val result = repositoryImpl.isUriExists(testUri)

        // then
        assertFalse(result)
    }

    @Test
    fun isUriExists_givenTrue_returnsTrue() {
        // given
        val testUri = "testUri"
        `when`(localDataSource.isUriExists(testUri)).thenReturn(true)

        // when
        val result = repositoryImpl.isUriExists(testUri)

        // then
        assertTrue(result)
    }

    @Test
    fun deleteUselessImages_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(localDataSource.deleteUselessImages()).thenReturn(Result.success(Unit))

            // when
            val result = repositoryImpl.deleteUselessImages()

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun deleteUselessImages_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(localDataSource.deleteUselessImages()).thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.deleteUselessImages()

            // then
            assertTrue(result.isFailure)
        }
    }
}