package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeTempLocalDataSource
import com.kdjj.domain.model.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class RecipeTempRepositoryImplTest {
    private lateinit var repositoryImpl: RecipeTempRepositoryImpl
    private lateinit var mockLocalDataSource: RecipeTempLocalDataSource

    private val dummyRecipeId = "test recipe id"
    private lateinit var mockRecipe: Recipe

    @Before
    fun setup() {
        mockLocalDataSource = mock(RecipeTempLocalDataSource::class.java)
        repositoryImpl = RecipeTempRepositoryImpl(mockLocalDataSource)
        mockRecipe = mock(Recipe::class.java)
    }

    @Test
    fun saveRecipeTemp_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.saveRecipeTemp(mockRecipe))
                .thenReturn(Result.success(Unit))

            // when
            val result = repositoryImpl.saveRecipeTemp(mockRecipe)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun saveRecipeTemp_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.saveRecipeTemp(mockRecipe))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.saveRecipeTemp(mockRecipe)

            // then
            assertTrue(result.isFailure)
        }
    }

    @Test
    fun deleteRecipeTemp_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.deleteRecipeTemp(dummyRecipeId))
                .thenReturn(Result.success(Unit))

            // when
            val result = repositoryImpl.deleteRecipeTemp(dummyRecipeId)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun deleteRecipeTemp_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.deleteRecipeTemp(dummyRecipeId))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.deleteRecipeTemp(dummyRecipeId)

            // then
            assertTrue(result.isFailure)
        }
    }

    @Test
    fun getRecipeTemp_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.getRecipeTemp(dummyRecipeId))
                .thenReturn(Result.success(mockRecipe))

            // when
            val result = repositoryImpl.getRecipeTemp(dummyRecipeId)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun getRecipeTemp_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(mockLocalDataSource.getRecipeTemp(dummyRecipeId))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.getRecipeTemp(dummyRecipeId)

            // then
            assertTrue(result.isFailure)
        }
    }
}