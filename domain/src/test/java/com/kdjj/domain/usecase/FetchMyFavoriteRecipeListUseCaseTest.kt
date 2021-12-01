package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchMyFavoriteRecipeListRequest
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeListRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.lang.Exception

class FetchMyFavoriteRecipeListUseCaseTest {

    private lateinit var mockRecipeListRepository: RecipeListRepository
    private lateinit var mockFetchMyFavoriteRecipeListRequest: FetchMyFavoriteRecipeListRequest
    private lateinit var fetchMyFavoriteRecipeListUseCase: FetchMyFavoriteRecipeListUseCase
    private lateinit var mockRecipe: Recipe

    @Before
    fun setup() {
        mockRecipeListRepository = mock(RecipeListRepository::class.java)
        mockFetchMyFavoriteRecipeListRequest = mock(FetchMyFavoriteRecipeListRequest::class.java)
        mockRecipe = mock(Recipe::class.java)
        fetchMyFavoriteRecipeListUseCase =
            FetchMyFavoriteRecipeListUseCase(mockRecipeListRepository)
    }

    @Test
    fun fetchMyFavoriteRecipeListUseCase_givenSuccess_returnSuccess(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeListRepository.fetchMyFavoriteRecipeListAfter(
                mockFetchMyFavoriteRecipeListRequest.index
            )
        ).thenReturn(Result.success(listOf(mockRecipe)))

        //given
        val testResult = fetchMyFavoriteRecipeListUseCase.invoke(mockFetchMyFavoriteRecipeListRequest)

        //then
        assertTrue(testResult.isSuccess)
    }

    @Test
    fun fetchMyFavoriteRecipeListUseCase_givenFailure_returnFailure(): Unit = runBlocking {
        //when
        `when`(
            mockRecipeListRepository.fetchMyFavoriteRecipeListAfter(
                mockFetchMyFavoriteRecipeListRequest.index
            )
        ).thenReturn(Result.failure(Exception()))

        //given
        val testResult = fetchMyFavoriteRecipeListUseCase.invoke(mockFetchMyFavoriteRecipeListRequest)

        //then
        assertTrue(testResult.isFailure)
    }
}