package com.kdjj.domain.usecase

import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.model.request.EmptyRequest
import com.kdjj.domain.repository.RecipeTypeRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.lang.Exception

class FetchRecipeTypeListUseCaseTest {

    private lateinit var mockRecipeTypeRepository: RecipeTypeRepository
    private lateinit var mockEmptyRequest: EmptyRequest
    private lateinit var mockRecipeType: RecipeType
    private lateinit var fetchRecipeTypeListUseCase: FetchRecipeTypeListUseCase

    @Before
    fun setup() {
        mockRecipeTypeRepository = mock(RecipeTypeRepository::class.java)
        mockEmptyRequest = mock(EmptyRequest::class.java)
        mockRecipeType = mock(RecipeType::class.java)
        fetchRecipeTypeListUseCase = FetchRecipeTypeListUseCase(mockRecipeTypeRepository)
    }

    @Test
    fun fetchRecipeTypeListUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //when
        `when`(mockRecipeTypeRepository.fetchRecipeTypeList())
            .thenReturn(Result.success(listOf(mockRecipeType)))

        //given
        val testResult = fetchRecipeTypeListUseCase.invoke(mockEmptyRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun fetchRecipeTypeListUseCase_givenFailure_returnFailure(): Unit = runBlocking {
        //when
        `when`(mockRecipeTypeRepository.fetchRecipeTypeList())
            .thenReturn(Result.failure(Exception()))

        //given
        val testResult = fetchRecipeTypeListUseCase.invoke(mockEmptyRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}