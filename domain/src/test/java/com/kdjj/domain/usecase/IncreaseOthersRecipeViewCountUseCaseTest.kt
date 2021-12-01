package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.IncreaseOthersRecipeViewCountRequest
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class IncreaseOthersRecipeViewCountUseCaseTest {

    private lateinit var mockRecipeRepository: RecipeRepository
    private lateinit var mockIncreaseOthersRecipeViewCountRequest: IncreaseOthersRecipeViewCountRequest
    private lateinit var increaseOthersRecipeViewCountUseCase: IncreaseOthersRecipeViewCountUseCase

    @Before
    fun setup() {
        mockRecipeRepository = mock(RecipeRepository::class.java)
        mockIncreaseOthersRecipeViewCountRequest =
            mock(IncreaseOthersRecipeViewCountRequest::class.java)
        increaseOthersRecipeViewCountUseCase =
            IncreaseOthersRecipeViewCountUseCase(mockRecipeRepository)
    }

    @Test
    fun increaseOthersRecipeViewCountUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeRepository.increaseOthersRecipeViewCount(
                mockIncreaseOthersRecipeViewCountRequest.recipe
            )
        ).thenReturn(Result.success(Unit))

        //given
        val testResult =
            increaseOthersRecipeViewCountUseCase.invoke(mockIncreaseOthersRecipeViewCountRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun increaseOthersRecipeViewCountUseCase_givenFailure_returnFailure(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeRepository.increaseOthersRecipeViewCount(
                mockIncreaseOthersRecipeViewCountRequest.recipe
            )
        ).thenReturn(Result.failure(Exception()))

        //given
        val testResult =
            increaseOthersRecipeViewCountUseCase.invoke(mockIncreaseOthersRecipeViewCountRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}