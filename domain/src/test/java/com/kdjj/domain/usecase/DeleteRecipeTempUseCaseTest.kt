package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.DeleteRecipeTempRequest
import com.kdjj.domain.repository.RecipeTempRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.lang.Exception

class DeleteRecipeTempUseCaseTest {

    private lateinit var mockTempRepository: RecipeTempRepository
    private lateinit var mockDeleteRecipeTempRequest: DeleteRecipeTempRequest
    private lateinit var deleteRecipeTempUseCase: DeleteRecipeTempUseCase

    @Before
    fun setup(){
        mockTempRepository = mock(RecipeTempRepository::class.java)
        mockDeleteRecipeTempRequest = mock(DeleteRecipeTempRequest::class.java)
        deleteRecipeTempUseCase = DeleteRecipeTempUseCase(mockTempRepository)
    }

    @Test
    fun deleteRecipeTempUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //given
        `when`(mockTempRepository.deleteRecipeTemp(mockDeleteRecipeTempRequest.recipeId))
            .thenReturn(Result.success(Unit))

        //when
        val testResult = deleteRecipeTempUseCase.invoke(mockDeleteRecipeTempRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun deleteRecipeTempUseCase_givenFailure_returnFailure(): Unit = runBlocking {
        //given
        `when`(mockTempRepository.deleteRecipeTemp(mockDeleteRecipeTempRequest.recipeId))
            .thenReturn(Result.failure(Exception()))

        //when
        val testResult = deleteRecipeTempUseCase.invoke(mockDeleteRecipeTempRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}