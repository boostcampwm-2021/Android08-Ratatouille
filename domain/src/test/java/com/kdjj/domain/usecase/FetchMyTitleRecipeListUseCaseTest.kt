package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchMyTitleRecipeListRequest
import com.kdjj.domain.repository.RecipeListRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class FetchMyTitleRecipeListUseCaseTest {

    private lateinit var mockRecipeListRepository: RecipeListRepository
    private lateinit var mockFetchMyTitleRecipeListRequest: FetchMyTitleRecipeListRequest
    private lateinit var mockRecipe: Recipe
    private lateinit var fetchMyTitleRecipeListUseCase: FetchMyTitleRecipeListUseCase

    @Before
    fun setup() {
        mockRecipeListRepository = mock(RecipeListRepository::class.java)
        mockFetchMyTitleRecipeListRequest = mock(FetchMyTitleRecipeListRequest::class.java)
        mockRecipe = mock(Recipe::class.java)
        fetchMyTitleRecipeListUseCase = FetchMyTitleRecipeListUseCase(mockRecipeListRepository)
    }

    @Test
    fun fetchMyTitleRecipeListUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeListRepository.fetchMyTitleRecipeListAfter(
                mockFetchMyTitleRecipeListRequest.index
            )
        ).thenReturn(Result.success(listOf(mockRecipe)))

        //given
        val testResult = fetchMyTitleRecipeListUseCase.invoke(mockFetchMyTitleRecipeListRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun fetchMyTitleRecipeListUseCase_givenFailure_returnFailure(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeListRepository.fetchMyTitleRecipeListAfter(
                mockFetchMyTitleRecipeListRequest.index
            )
        ).thenReturn(Result.failure(Exception()))

        //given
        val testResult = fetchMyTitleRecipeListUseCase.invoke(mockFetchMyTitleRecipeListRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}