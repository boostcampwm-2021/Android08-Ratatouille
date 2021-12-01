package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchOthersLatestRecipeListRequest
import com.kdjj.domain.repository.RecipeListRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class FetchOthersLatestRecipeListUseCaseTest {

    private lateinit var mockRecipeListRepository: RecipeListRepository
    private lateinit var mockFetchOthersLatestRecipeListRequest: FetchOthersLatestRecipeListRequest
    private lateinit var mockRecipe: Recipe
    private lateinit var fetchOthersLatestRecipeListUseCase: FetchOthersLatestRecipeListUseCase

    @Before
    fun setup() {
        mockRecipeListRepository = mock(RecipeListRepository::class.java)
        mockFetchOthersLatestRecipeListRequest =
            mock(FetchOthersLatestRecipeListRequest::class.java)
        mockRecipe = mock(Recipe::class.java)
        fetchOthersLatestRecipeListUseCase =
            FetchOthersLatestRecipeListUseCase(mockRecipeListRepository)
    }

    @Test
    fun fetchOthersLatestRecipeListUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeListRepository.fetchOthersLatestRecipeListAfter(
                mockFetchOthersLatestRecipeListRequest.refresh
            )
        ).thenReturn(Result.success(listOf(mockRecipe)))

        //given
        val testResult = fetchOthersLatestRecipeListUseCase.invoke(mockFetchOthersLatestRecipeListRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun fetchOthersLatestRecipeListUseCase_givenFailure_returnFailure(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeListRepository.fetchOthersLatestRecipeListAfter(
                mockFetchOthersLatestRecipeListRequest.refresh
            )
        ).thenReturn(Result.failure(Exception()))

        //given
        val testResult = fetchOthersLatestRecipeListUseCase.invoke(mockFetchOthersLatestRecipeListRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}