package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeLocalDataSource
import com.kdjj.data.datasource.RecipeRemoteDataSource
import com.kdjj.domain.model.*
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.*

class RecipeRepositoryImplTest {

    private lateinit var repositoryImpl: RecipeRepositoryImpl
    private lateinit var mockLocalDataSource: RecipeLocalDataSource
    private lateinit var mockRemoteDataSource: RecipeRemoteDataSource

    private val dummyRecipeId = "test recipe id"
    private lateinit var mockRecipe: Recipe

    @Before
    fun setup() {
        mockLocalDataSource = mock(RecipeLocalDataSource::class.java)
        mockRemoteDataSource = mock(RecipeRemoteDataSource::class.java)
        repositoryImpl = RecipeRepositoryImpl(mockLocalDataSource, mockRemoteDataSource)
        mockRecipe = mock(Recipe::class.java)
    }

    @Test
    fun saveMyRecipe_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.saveRecipe(mockRecipe))
                .thenReturn(Result.success(Unit))

            // when
            val updatedFlow = repositoryImpl.getRecipeUpdateFlow()
            val oldUpdatedValue = updatedFlow.last()
            val result = repositoryImpl.saveMyRecipe(mockRecipe)

            // then
            assertTrue(result.isSuccess)
            assertEquals(oldUpdatedValue + 1, updatedFlow.last())
        }
    }

    @Test
    fun saveMyRecipe_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.saveRecipe(mockRecipe))
                .thenReturn(Result.failure(Exception()))

            // when
            val updatedFlow = repositoryImpl.getRecipeUpdateFlow()
            val oldUpdatedValue = updatedFlow.last()
            val result = repositoryImpl.saveMyRecipe(mockRecipe)

            // then
            assertTrue(result.isFailure)
            assertEquals(oldUpdatedValue, updatedFlow.last())
        }
    }

    @Test
    fun updateMyRecipe_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.updateRecipe(mockRecipe))
                .thenReturn(Result.success(Unit))

            // when
            val updatedFlow = repositoryImpl.getRecipeUpdateFlow()
            val oldUpdatedValue = updatedFlow.last()
            val result = repositoryImpl.updateMyRecipe(mockRecipe)

            // then
            assertTrue(result.isSuccess)
            assertEquals(oldUpdatedValue + 1, updatedFlow.last())
        }
    }

    @Test
    fun updateMyRecipe_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.updateRecipe(mockRecipe))
                .thenReturn(Result.failure(Exception()))

            // when
            val updatedFlow = repositoryImpl.getRecipeUpdateFlow()
            val oldUpdatedValue = updatedFlow.last()
            val result = repositoryImpl.updateMyRecipe(mockRecipe)

            // then
            assertTrue(result.isFailure)
            assertEquals(oldUpdatedValue, updatedFlow.last())
        }
    }

    @Test
    fun updateMyRecipe_withImageGivenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.updateRecipe(mockRecipe, anyList()))
                .thenReturn(Result.success(Unit))

            // when
            val updatedFlow = repositoryImpl.getRecipeUpdateFlow()
            val oldUpdatedValue = updatedFlow.last()
            val result = repositoryImpl.updateMyRecipe(mockRecipe, listOf())

            // then
            assertTrue(result.isSuccess)
            assertEquals(oldUpdatedValue + 1, updatedFlow.last())
        }
    }

    @Test
    fun updateMyRecipe_withImageGivenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.updateRecipe(mockRecipe, anyList()))
                .thenReturn(Result.failure(Exception()))

            // when
            val updatedFlow = repositoryImpl.getRecipeUpdateFlow()
            val oldUpdatedValue = updatedFlow.last()
            val result = repositoryImpl.updateMyRecipe(mockRecipe, listOf())

            // then
            assertTrue(result.isFailure)
            assertEquals(oldUpdatedValue, updatedFlow.last())
        }
    }

    @Test
    fun deleteMyRecipe_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.deleteRecipe(mockRecipe))
                .thenReturn(Result.success(Unit))

            // when
            val updatedFlow = repositoryImpl.getRecipeUpdateFlow()
            val oldUpdatedValue = updatedFlow.last()
            val result = repositoryImpl.deleteMyRecipe(mockRecipe)

            // then
            assertTrue(result.isSuccess)
            assertEquals(oldUpdatedValue + 1, updatedFlow.last())
        }
    }

    @Test
    fun deleteMyRecipe_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.deleteRecipe(mockRecipe))
                .thenReturn(Result.failure(Exception()))

            // when
            val updatedFlow = repositoryImpl.getRecipeUpdateFlow()
            val oldUpdatedValue = updatedFlow.last()
            val result = repositoryImpl.deleteMyRecipe(mockRecipe)

            // then
            assertTrue(result.isFailure)
            assertEquals(oldUpdatedValue, updatedFlow.last())
        }
    }

    @Test
    fun uploadRecipe_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(mockRemoteDataSource.uploadRecipe(mockRecipe))
                .thenReturn(Result.success(Unit))

            // when
            val result = repositoryImpl.uploadRecipe(mockRecipe)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun uploadRecipe_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(mockRemoteDataSource.uploadRecipe(mockRecipe))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.uploadRecipe(mockRecipe)

            // then
            assertTrue(result.isFailure)
        }
    }

    @Test
    fun increaseOthersRecipeViewCount_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(mockRemoteDataSource.increaseViewCount(mockRecipe))
                .thenReturn(Result.success(Unit))

            // when
            val result = repositoryImpl.increaseOthersRecipeViewCount(mockRecipe)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun increaseOthersRecipeViewCount_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(mockRemoteDataSource.increaseViewCount(mockRecipe))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.increaseOthersRecipeViewCount(mockRecipe)

            // then
            assertTrue(result.isFailure)
        }
    }

    @Test
    fun deleteOthersRecipe_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(mockRemoteDataSource.deleteRecipe(mockRecipe))
                .thenReturn(Result.success(Unit))

            // when
            val result = repositoryImpl.deleteOthersRecipe(mockRecipe)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun deleteOthersRecipe_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(mockRemoteDataSource.deleteRecipe(mockRecipe))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.deleteOthersRecipe(mockRecipe)

            // then
            assertTrue(result.isFailure)
        }
    }

    @Test
    fun fetchOthersRecipe_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(mockRemoteDataSource.fetchRecipe(dummyRecipeId))
                .thenReturn(Result.success(mockRecipe))

            // when
            val result = repositoryImpl.fetchOthersRecipe(dummyRecipeId)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun fetchOthersRecipe_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(mockRemoteDataSource.fetchRecipe(dummyRecipeId))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.fetchOthersRecipe(dummyRecipeId)

            // then
            assertTrue(result.isFailure)
        }
    }

    @Test
    fun getLocalRecipe_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.getRecipe(dummyRecipeId))
                .thenReturn(Result.success(mockRecipe))

            // when
            val result = repositoryImpl.getLocalRecipe(dummyRecipeId)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun getLocalRecipe_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.getRecipe(dummyRecipeId))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.getLocalRecipe(dummyRecipeId)

            // then
            assertTrue(result.isFailure)
        }
    }
}