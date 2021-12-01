package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchMySearchRecipeListRequest
import com.kdjj.domain.repository.RecipeListRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class FetchMySearchRecipeListUseCaseTest {

    private lateinit var mockRecipeListRepository: RecipeListRepository
    private lateinit var mockFetchMySearchRecipeListRequest: FetchMySearchRecipeListRequest
    private lateinit var mockRecipe: Recipe
    private lateinit var fetchMySearchRecipeListUseCase: FetchMySearchRecipeListUseCase

    @Before
    fun setup() {
        mockRecipeListRepository = mock(RecipeListRepository::class.java)
        mockFetchMySearchRecipeListRequest = mock(FetchMySearchRecipeListRequest::class.java)
        mockRecipe = mock(Recipe::class.java)
        fetchMySearchRecipeListUseCase = FetchMySearchRecipeListUseCase(mockRecipeListRepository)
    }

    @Test
    fun fetchMySearchRecipeListUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeListRepository.fetchMySearchRecipeListAfter(
                mockFetchMySearchRecipeListRequest.keyword,
                mockFetchMySearchRecipeListRequest.index
            )
        ).thenReturn(Result.success(listOf(mockRecipe)))

        //given
        val testResult = fetchMySearchRecipeListUseCase(mockFetchMySearchRecipeListRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun fetchMySearchRecipeListUseCase_givenFailure_returnFailure(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeListRepository.fetchMySearchRecipeListAfter(
                mockFetchMySearchRecipeListRequest.keyword,
                mockFetchMySearchRecipeListRequest.index
            )
        ).thenReturn(Result.failure(Exception()))

        //given
        val testResult = fetchMySearchRecipeListUseCase(mockFetchMySearchRecipeListRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}