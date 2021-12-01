package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeListLocalDataSource
import com.kdjj.data.datasource.RecipeListRemoteDataSource
import com.kdjj.domain.model.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class RecipeListRepositoryImplTest {

    private lateinit var repositoryImpl: RecipeListRepositoryImpl
    private lateinit var localDataSource: RecipeListLocalDataSource
    private lateinit var remoteDataSource: RecipeListRemoteDataSource

    private lateinit var mockRecipe: Recipe
    @Before
    fun setup() {
        localDataSource = mock(RecipeListLocalDataSource::class.java)
        remoteDataSource = mock(RecipeListRemoteDataSource::class.java)
        repositoryImpl = RecipeListRepositoryImpl(remoteDataSource, localDataSource)
        mockRecipe = mock(Recipe::class.java)
    }

    @Test
    fun fetchOthersLatestRecipeListAfter_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(remoteDataSource.fetchLatestRecipeListAfter(anyBoolean()))
                .thenReturn(Result.success(listOf(mockRecipe)))

            // when
            val result = repositoryImpl.fetchOthersLatestRecipeListAfter(true)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun fetchOthersLatestRecipeListAfter_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(remoteDataSource.fetchLatestRecipeListAfter(anyBoolean()))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.fetchOthersLatestRecipeListAfter(true)

            // then
            assertTrue(result.isFailure)
        }
    }

    @Test
    fun fetchOthersPopularRecipeListAfter_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(remoteDataSource.fetchPopularRecipeListAfter(anyBoolean()))
                .thenReturn(Result.success(listOf(mockRecipe)))

            // when
            val result = repositoryImpl.fetchOthersPopularRecipeListAfter(true)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun fetchOthersPopularRecipeListAfter_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(remoteDataSource.fetchPopularRecipeListAfter(anyBoolean()))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.fetchOthersPopularRecipeListAfter(true)

            // then
            assertTrue(result.isFailure)
        }
    }

    @Test
    fun fetchOthersSearchRecipeListAfter_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(remoteDataSource.fetchSearchRecipeListAfter(anyString(), anyBoolean()))
                .thenReturn(Result.success(listOf(mockRecipe)))

            // when
            val result = repositoryImpl.fetchOthersSearchRecipeListAfter("test keyword", true)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun fetchOthersSearchRecipeListAfter_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(remoteDataSource.fetchSearchRecipeListAfter(anyString(), anyBoolean()))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.fetchOthersSearchRecipeListAfter("test keyword", true)

            // then
            assertTrue(result.isFailure)
        }
    }

    @Test
    fun fetchMyLatestRecipeListAfter_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(localDataSource.fetchLatestRecipeListAfter(anyInt()))
                .thenReturn(Result.success(listOf(mockRecipe)))

            // when
            val result = repositoryImpl.fetchMyLatestRecipeListAfter(0)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun fetchMyLatestRecipeListAfter_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(localDataSource.fetchLatestRecipeListAfter(anyInt()))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.fetchMyLatestRecipeListAfter(0)

            // then
            assertTrue(result.isFailure)
        }
    }

    @Test
    fun fetchMyFavoriteRecipeListAfter_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(localDataSource.fetchFavoriteRecipeListAfter(anyInt()))
                .thenReturn(Result.success(listOf(mockRecipe)))

            // when
            val result = repositoryImpl.fetchMyFavoriteRecipeListAfter(0)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun fetchMyFavoriteRecipeListAfter_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(localDataSource.fetchFavoriteRecipeListAfter(anyInt()))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.fetchMyFavoriteRecipeListAfter(0)

            // then
            assertTrue(result.isFailure)
        }
    }

    @Test
    fun fetchMySearchRecipeListAfter_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(localDataSource.fetchSearchRecipeListAfter(anyString(), anyInt()))
                .thenReturn(Result.success(listOf(mockRecipe)))

            // when
            val result = repositoryImpl.fetchMySearchRecipeListAfter("test keyword", 0)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun fetchMySearchRecipeListAfter_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(localDataSource.fetchSearchRecipeListAfter(anyString(), anyInt()))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.fetchMySearchRecipeListAfter("test keyword", 0)

            // then
            assertTrue(result.isFailure)
        }
    }

    @Test
    fun fetchMyTitleRecipeListAfter_givenSuccess_returnsSuccess() {
        runBlocking {
            // given
            `when`(localDataSource.fetchTitleListAfter(anyInt()))
                .thenReturn(Result.success(listOf(mockRecipe)))

            // when
            val result = repositoryImpl.fetchMyTitleRecipeListAfter(0)

            // then
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun fetchMyTitleRecipeListAfter_givenFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(localDataSource.fetchTitleListAfter(anyInt()))
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.fetchMyTitleRecipeListAfter(0)

            // then
            assertTrue(result.isFailure)
        }
    }
}