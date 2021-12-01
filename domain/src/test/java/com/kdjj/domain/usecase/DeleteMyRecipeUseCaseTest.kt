package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.DeleteMyRecipeRequest
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.lang.Exception

class DeleteMyRecipeUseCaseTest {

    private lateinit var mockRecipeRepository: RecipeRepository
    private lateinit var mockDeleteMyRecipeRequest: DeleteMyRecipeRequest
    private lateinit var deleteMyRecipeUseCase: DeleteMyRecipeUseCase

    @Before
    fun setup() {
        mockRecipeRepository = mock(RecipeRepository::class.java)
        mockDeleteMyRecipeRequest = mock(DeleteMyRecipeRequest::class.java)
        deleteMyRecipeUseCase = DeleteMyRecipeUseCase(mockRecipeRepository)
    }

    @Test
    fun deleteMyRecipeUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //given
        `when`(mockRecipeRepository.deleteMyRecipe(mockDeleteMyRecipeRequest.recipe))
            .thenReturn(Result.success(Unit))

        //when
        val testResult = deleteMyRecipeUseCase.invoke(mockDeleteMyRecipeRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun deleteMyRecipeUseCase_givenFailure_returnFailure(): Unit = runBlocking {
        //given
        `when`(mockRecipeRepository.deleteMyRecipe(mockDeleteMyRecipeRequest.recipe))
            .thenReturn(Result.failure(Exception()))

        //when
        val testResult = deleteMyRecipeUseCase.invoke(mockDeleteMyRecipeRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}