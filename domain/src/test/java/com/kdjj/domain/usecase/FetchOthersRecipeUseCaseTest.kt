package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchOthersRecipeRequest
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class FetchOthersRecipeUseCaseTest {

    private lateinit var mockRecipeRepository: RecipeRepository
    private lateinit var mockFetchOthersRecipeRequest: FetchOthersRecipeRequest
    private lateinit var mockRecipe: Recipe
    private lateinit var fetchOthersRecipeUseCase: FetchOthersRecipeUseCase

    @Before
    fun setup() {
        mockRecipeRepository = mock(RecipeRepository::class.java)
        mockFetchOthersRecipeRequest = mock(FetchOthersRecipeRequest::class.java)
        mockRecipe = mock(Recipe::class.java)
        fetchOthersRecipeUseCase = FetchOthersRecipeUseCase(mockRecipeRepository)
    }

    @Test
    fun fetchOthersRecipeUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //when
        `when`(mockRecipeRepository.fetchOthersRecipe(mockFetchOthersRecipeRequest.recipeId))
            .thenReturn(Result.success(mockRecipe))

        //given
        val testResult = fetchOthersRecipeUseCase.invoke(mockFetchOthersRecipeRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun fetchOthersRecipeUseCase_givenFailure_returnFailue(): Unit = runBlocking {
        //when
        `when`(mockRecipeRepository.fetchOthersRecipe(mockFetchOthersRecipeRequest.recipeId))
            .thenReturn(Result.failure(Exception()))

        //given
        val testResult = fetchOthersRecipeUseCase.invoke(mockFetchOthersRecipeRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}