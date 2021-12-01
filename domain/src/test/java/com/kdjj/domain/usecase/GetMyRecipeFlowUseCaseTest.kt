package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.GetMyRecipeRequest
import com.kdjj.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class GetMyRecipeFlowUseCaseTest {

    private lateinit var mockRecipeRepository: RecipeRepository
    private lateinit var mockGetMyRecipeRequest: GetMyRecipeRequest
    private lateinit var mockRecipe: Recipe
    private lateinit var getMyRecipeFlowUseCase: GetMyRecipeFlowUseCase

    @Before
    fun setup(){
        mockRecipeRepository = mock(RecipeRepository::class.java)
        mockGetMyRecipeRequest = mock(GetMyRecipeRequest::class.java)
        mockRecipe = mock(Recipe::class.java)
        getMyRecipeFlowUseCase = GetMyRecipeFlowUseCase(mockRecipeRepository)
    }

    @Test
    fun getMyRecipeFlowUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //when
        `when`(mockRecipeRepository.getMyRecipeFlow(mockGetMyRecipeRequest.recipeId))
            .thenReturn(flowOf(mockRecipe))

        //given
        val testResult = getMyRecipeFlowUseCase.invoke(mockGetMyRecipeRequest)

        //then
        assertEquals(mockRecipe, testResult.first())
    }
}