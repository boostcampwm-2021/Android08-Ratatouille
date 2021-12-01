package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchMyLatestRecipeListRequest
import com.kdjj.domain.repository.RecipeListRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class FetchMyLatestRecipeListUseCaseTest {

    private lateinit var mockRecipeListRepository: RecipeListRepository
    private lateinit var mockFetchMyLatestRecipeListRequest: FetchMyLatestRecipeListRequest
    private lateinit var mockRecipe: Recipe
    private lateinit var fetchMyLatestRecipeListUseCase: FetchMyLatestRecipeListUseCase

    @Before
    fun setup() {
        mockRecipeListRepository = mock(RecipeListRepository::class.java)
        mockFetchMyLatestRecipeListRequest = mock(FetchMyLatestRecipeListRequest::class.java)
        mockRecipe = mock(Recipe::class.java)
        fetchMyLatestRecipeListUseCase = FetchMyLatestRecipeListUseCase(mockRecipeListRepository)
    }

    @Test
    fun fetchMyLatestRecipeListUseCaseTest_givenSuccess_returnSuccess(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeListRepository.fetchMyLatestRecipeListAfter(
                mockFetchMyLatestRecipeListRequest.index
            )
        ).thenReturn(Result.success(listOf(mockRecipe)))

        //given
        val testResult = fetchMyLatestRecipeListUseCase.invoke(mockFetchMyLatestRecipeListRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun fetchMyLatestRecipeListUseCaseTest_givenFailure_returnFailure(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeListRepository.fetchMyLatestRecipeListAfter(
                mockFetchMyLatestRecipeListRequest.index
            )
        ).thenReturn(Result.failure(Exception()))

        //given
        val testResult = fetchMyLatestRecipeListUseCase.invoke(mockFetchMyLatestRecipeListRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}