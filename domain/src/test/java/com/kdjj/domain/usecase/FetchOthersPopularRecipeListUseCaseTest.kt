package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchOthersPopularRecipeListRequest
import com.kdjj.domain.repository.RecipeListRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.lang.Exception

class FetchOthersPopularRecipeListUseCaseTest {

    private lateinit var mockRecipeListRepository: RecipeListRepository
    private lateinit var mockFetchOthersPopularRecipeListRequest: FetchOthersPopularRecipeListRequest
    private lateinit var mockRecipe: Recipe
    private lateinit var fetchOthersPopularRecipeListUseCase: FetchOthersPopularRecipeListUseCase

    @Before
    fun setup() {
        mockRecipeListRepository = mock(RecipeListRepository::class.java)
        mockFetchOthersPopularRecipeListRequest =
            mock(FetchOthersPopularRecipeListRequest::class.java)
        mockRecipe = mock(Recipe::class.java)
        fetchOthersPopularRecipeListUseCase =
            FetchOthersPopularRecipeListUseCase(mockRecipeListRepository)
    }

    @Test
    fun fetchOthersPopularRecipeListUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeListRepository.fetchOthersPopularRecipeListAfter(
                mockFetchOthersPopularRecipeListRequest.refresh
            )
        ).thenReturn(Result.success(listOf(mockRecipe)))

        //given
        val testResult = fetchOthersPopularRecipeListUseCase.invoke(mockFetchOthersPopularRecipeListRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun fetchOthersPopularRecipeListUseCase_givenFailure_returnFailure(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeListRepository.fetchOthersPopularRecipeListAfter(
                mockFetchOthersPopularRecipeListRequest.refresh
            )
        ).thenReturn(Result.failure(Exception()))

        //given
        val testResult = fetchOthersPopularRecipeListUseCase.invoke(mockFetchOthersPopularRecipeListRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}