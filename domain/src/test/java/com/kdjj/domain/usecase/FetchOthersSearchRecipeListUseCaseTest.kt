package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchOthersSearchRecipeListRequest
import com.kdjj.domain.repository.RecipeListRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class FetchOthersSearchRecipeListUseCaseTest {

    private lateinit var mockRecipeListRepository: RecipeListRepository
    private lateinit var mockFetchOthersSearchRecipeListRequest: FetchOthersSearchRecipeListRequest
    private lateinit var mockRecipe: Recipe
    private lateinit var fetchOthersSearchRecipeListUseCase: FetchOthersSearchRecipeListUseCase

    @Before
    fun setup() {
        mockRecipeListRepository = mock(RecipeListRepository::class.java)
        mockRecipe = mock(Recipe::class.java)
        mockFetchOthersSearchRecipeListRequest =
            mock(FetchOthersSearchRecipeListRequest::class.java)
        fetchOthersSearchRecipeListUseCase =
            FetchOthersSearchRecipeListUseCase(mockRecipeListRepository)
    }

    @Test
    fun fetchOthersSearchRecipeListUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeListRepository.fetchOthersSearchRecipeListAfter(
                mockFetchOthersSearchRecipeListRequest.keyword,
                mockFetchOthersSearchRecipeListRequest.refresh
            )
        ).thenReturn(Result.success(listOf(mockRecipe)))

        //given
        val testResult =
            fetchOthersSearchRecipeListUseCase.invoke(mockFetchOthersSearchRecipeListRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun fetchOthersSearchRecipeListUseCase_givenFailure_returnFailure(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeListRepository.fetchOthersSearchRecipeListAfter(
                mockFetchOthersSearchRecipeListRequest.keyword,
                mockFetchOthersSearchRecipeListRequest.refresh
            )
        ).thenReturn(Result.failure(Exception()))

        //given
        val testResult =
            fetchOthersSearchRecipeListUseCase.invoke(mockFetchOthersSearchRecipeListRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}
