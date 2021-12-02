package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.EmptyRequest
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class GetRecipeUpdateFlowUseCaseTest {

    private lateinit var mockRecipeRepository: RecipeRepository
    private lateinit var mockEmptyRequest: EmptyRequest
    private lateinit var getRecipeUpdateFlowUseCase: GetRecipeUpdateFlowUseCase

    @Before
    fun setup(){
        mockRecipeRepository = mock(RecipeRepository::class.java)
        mockEmptyRequest = mock(EmptyRequest::class.java)
        getRecipeUpdateFlowUseCase = GetRecipeUpdateFlowUseCase(mockRecipeRepository)
    }

    @Test
    fun getRecipeUpdateFlowUseCase_givenFlowInt_returnFlowInt(): Unit = runBlocking {
        //when
        `when`(mockRecipeRepository.getRecipeUpdateFlow()).thenReturn(flowOf(Unit))

        //given
        val testResult = getRecipeUpdateFlowUseCase.invoke(mockEmptyRequest)

        //then
        assertEquals(Unit, testResult.first())
    }
}