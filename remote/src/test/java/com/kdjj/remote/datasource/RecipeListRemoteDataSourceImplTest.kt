package com.kdjj.remote.datasource

import com.kdjj.domain.model.*
import com.kdjj.remote.dto.toDto
import com.kdjj.remote.service.RemoteRecipeListService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class RecipeListRemoteDataSourceImplTest {

    private lateinit var mockRecipeListService: RemoteRecipeListService
    private lateinit var mockRecipeListRemoteDataSourceImpl: RecipeListRemoteDataSourceImpl
    private val dummyRecipeId = "dummyRecipeId1"
    private val dummyRecipeType = RecipeType(1, "DummyRecipeType")
    private val dummyRecipeStep1 = RecipeStep(
        stepId = "dummyStepId1",
        name = "dummyStep1",
        type = RecipeStepType.values().first(),
        description = "dummyStepDescription",
        imgPath = "",
        seconds = 0
    )
    private val dummyRecipe = Recipe(
        recipeId = dummyRecipeId,
        title = "dummyRecipeTitle1",
        type = dummyRecipeType,
        stuff = "",
        imgPath = "",
        stepList = listOf(dummyRecipeStep1),
        authorId = "",
        viewCount = 0,
        isFavorite = false,
        createTime = 0,
        state = RecipeState.NETWORK
    )
    private val dummyRecipeList = listOf(
        dummyRecipe
    )

    @Before
    fun setUp() { // given
        mockRecipeListService = mock(RemoteRecipeListService::class.java)
        mockRecipeListRemoteDataSourceImpl = RecipeListRemoteDataSourceImpl(mockRecipeListService)
    }

    @Test
    fun fetchLatestRecipeListAfter_giveBoolean_successAndRecipeList(): Unit = runBlocking {
        // given
        `when`(mockRecipeListService.fetchLatestRecipeListAfter(anyBoolean()))
            .thenReturn(dummyRecipeList.map { it.toDto() })

        // when
        val fetchLatestRecipeListAfterResult =
            mockRecipeListRemoteDataSourceImpl.fetchLatestRecipeListAfter(anyBoolean())

        // then
        assertTrue(fetchLatestRecipeListAfterResult.isSuccess)
        assertEquals(dummyRecipeList, fetchLatestRecipeListAfterResult.getOrNull())
    }

    @Test
    fun fetchPopularRecipeListAfter_giveBoolean_successAndRecipeList(): Unit = runBlocking {
        // given
        `when`(mockRecipeListService.fetchPopularRecipeListAfter(anyBoolean()))
            .thenReturn(dummyRecipeList.map { it.toDto() })

        // when
        val fetchPopularRecipeListAfter = mockRecipeListRemoteDataSourceImpl.fetchPopularRecipeListAfter(anyBoolean())

        // then
        assertTrue(fetchPopularRecipeListAfter.isSuccess)
        assertEquals(dummyRecipeList, fetchPopularRecipeListAfter.getOrNull())
    }

    @Test
    fun fetchSearchRecipeListAfter_giveBoolean_successAndRecipeList(): Unit = runBlocking {
        // given
        `when`(mockRecipeListService.fetchSearchRecipeListAfter(anyString(), anyBoolean()))
            .thenReturn(dummyRecipeList.map { it.toDto() })

        // when
        val fetchSearchRecipeListAfterResult =
            mockRecipeListRemoteDataSourceImpl.fetchSearchRecipeListAfter(anyString(), anyBoolean())

        // then
        assertTrue(fetchSearchRecipeListAfterResult.isSuccess)
        assertEquals(dummyRecipeList, fetchSearchRecipeListAfterResult.getOrNull())
    }
}