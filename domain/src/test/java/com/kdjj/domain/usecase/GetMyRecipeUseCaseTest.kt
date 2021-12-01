package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.GetMyRecipeRequest
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class GetMyRecipeUseCaseTest {

    private lateinit var mockRecipeRepository: RecipeRepository
    private lateinit var mockGetMyRecipeRequest: GetMyRecipeRequest
    private lateinit var mockRecipe: Recipe
    private lateinit var getMyRecipeUseCase: GetMyRecipeUseCase

    @Before
    fun setup(){
        mockRecipeRepository = mock(RecipeRepository::class.java)
        mockGetMyRecipeRequest = mock(GetMyRecipeRequest::class.java)
        mockRecipe = mock(Recipe::class.java)
        getMyRecipeUseCase = GetMyRecipeUseCase(mockRecipeRepository)
    }

    @Test
    fun getMyRecipeUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //when
        `when`(mockRecipeRepository.getLocalRecipe(mockGetMyRecipeRequest.recipeId))
            .thenReturn(Result.success(mockRecipe))

        //given
        val testResult = getMyRecipeUseCase.invoke(mockGetMyRecipeRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun getMyRecipeUseCase_givenFailure_returnFailure(): Unit = runBlocking {
        //when
        `when`(mockRecipeRepository.getLocalRecipe(mockGetMyRecipeRequest.recipeId))
            .thenReturn(Result.failure(Exception()))

        //given
        val testResult = getMyRecipeUseCase.invoke(mockGetMyRecipeRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}