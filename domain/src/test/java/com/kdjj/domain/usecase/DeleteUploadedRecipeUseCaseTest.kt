package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.DeleteUploadedRecipeRequest
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.lang.Exception

class DeleteUploadedRecipeUseCaseTest{

    private lateinit var mockRecipeRepository: RecipeRepository
    private lateinit var mockDeleteUploadedRecipeRequest: DeleteUploadedRecipeRequest
    private lateinit var deleteUploadedRecipeUseCase: DeleteUploadedRecipeUseCase

    @Before
    fun setup(){
        mockRecipeRepository = mock(RecipeRepository::class.java)
        mockDeleteUploadedRecipeRequest = mock(DeleteUploadedRecipeRequest::class.java)
        deleteUploadedRecipeUseCase = DeleteUploadedRecipeUseCase(mockRecipeRepository)
    }

    @Test
    fun deleteUploadedRecipeUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //when
        `when`(mockRecipeRepository.deleteOthersRecipe(mockDeleteUploadedRecipeRequest.recipe))
            .thenReturn(Result.success(Unit))

        //given
        val testResult = deleteUploadedRecipeUseCase.invoke(mockDeleteUploadedRecipeRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun deleteUploadedRecipeUseCase_givenFailure_returnFailure(): Unit = runBlocking {
        //when
        `when`(mockRecipeRepository.deleteOthersRecipe(mockDeleteUploadedRecipeRequest.recipe))
            .thenReturn(Result.failure(Exception()))

        //given
        val testResult = deleteUploadedRecipeUseCase.invoke(mockDeleteUploadedRecipeRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}