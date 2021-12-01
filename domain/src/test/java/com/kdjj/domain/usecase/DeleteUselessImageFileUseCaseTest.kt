package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.EmptyRequest
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.lang.Exception

class DeleteUselessImageFileUseCaseTest {

    private lateinit var mockRecipeImageRepository: RecipeImageRepository
    private lateinit var mockEmptyRequest: EmptyRequest
    private lateinit var deleteUselessImageFileUseCase: DeleteUselessImageFileUseCase

    @Before
    fun setup() {
        mockRecipeImageRepository = mock(RecipeImageRepository::class.java)
        mockEmptyRequest = mock(EmptyRequest::class.java)
        deleteUselessImageFileUseCase = DeleteUselessImageFileUseCase(mockRecipeImageRepository)
    }

    @Test
    fun deleteUselessImageFileUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //when
        `when`(mockRecipeImageRepository.deleteUselessImages()).thenReturn(Result.success(Unit))

        //given
        val testResult = deleteUselessImageFileUseCase.invoke(mockEmptyRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun deleteUselessImageFileUseCase_givenFailure_returnFailure(): Unit = runBlocking {
        //when
        `when`(mockRecipeImageRepository.deleteUselessImages()).thenReturn(Result.failure(Exception()))

        //given
        val testResult = deleteUselessImageFileUseCase.invoke(mockEmptyRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}