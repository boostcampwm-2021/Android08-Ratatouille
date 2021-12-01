package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeTypeLocalDataSource
import com.kdjj.data.datasource.RecipeTypeRemoteDataSource
import com.kdjj.domain.model.RecipeType
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class RecipeTypeRepositoryImplTest {
    private lateinit var repositoryImpl: RecipeTypeRepositoryImpl
    private lateinit var localDataSource: RecipeTypeLocalDataSource
    private lateinit var remoteDataSource: RecipeTypeRemoteDataSource

    @Before
    fun setup() {
        localDataSource = mock(RecipeTypeLocalDataSource::class.java)
        remoteDataSource = mock(RecipeTypeRemoteDataSource::class.java)
        repositoryImpl = RecipeTypeRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun fetchRecipeTypeList_remoteSuccess_callsLocalSave() {
        runBlocking {
            // given
            val testTypeList = listOf(RecipeType(1, "success"))
            `when`(remoteDataSource.fetchRecipeTypeList())
                .thenReturn(Result.success(testTypeList))

            // when
            val result = repositoryImpl.fetchRecipeTypeList()

            // then
            assertTrue(result.isSuccess)
            verify(localDataSource).saveRecipeTypeList(testTypeList)
        }
    }

    @Test
    fun fetchRecipeTypeList_remoteFailure_fetchLocal() {
        runBlocking {
            // given
            val testTypeList = listOf(RecipeType(1, "success"))
            `when`(remoteDataSource.fetchRecipeTypeList())
                .thenReturn(Result.failure(Exception()))
            `when`(localDataSource.fetchRecipeTypeList())
                .thenReturn(Result.success(testTypeList))

            // when
            val result = repositoryImpl.fetchRecipeTypeList()

            // then
            assertTrue(result.isSuccess)
            verify(localDataSource).fetchRecipeTypeList()
        }
    }

    @Test
    fun fetchRecipeTypeList_bothFailure_returnsFailure() {
        runBlocking {
            // given
            `when`(remoteDataSource.fetchRecipeTypeList())
                .thenReturn(Result.failure(Exception()))
            `when`(localDataSource.fetchRecipeTypeList())
                .thenReturn(Result.failure(Exception()))

            // when
            val result = repositoryImpl.fetchRecipeTypeList()

            // then
            assertTrue(result.isFailure)
        }
    }
}