package com.kdjj.local.dataSource

import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.RecipeStepType
import com.kdjj.local.dao.RecipeListDao
import com.kdjj.local.dto.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RecipeListLocalDataSourceImplTest {

    private lateinit var mockRecipeListDao: RecipeListDao
    private lateinit var recipeListLocalDataSourceImpl: RecipeListLocalDataSourceImpl

    private val testIndex = 0
    private val testPageSize = 10
    private val testKeyword = "testKeyword"

    private val recipeTypeDto = RecipeTypeDto(1L, "한식")

    private val recipeMeta = RecipeMetaDto(
        "recipeId",
        "두둥탁! 맛있는 감자탕!!",
        "stuff",
        "image path",
        "authorId",
        false,
        1000,
        RecipeState.CREATE,
        1L,
    )

    private val recipeStepDto = RecipeStepDto(
        "stepId",
        "삶기",
        1,
        RecipeStepType.FRY,
        "description",
        "image path",
        1000,
        "recipeId"
    )

    private val recipeDto = RecipeDto(
        recipeMeta,
        recipeTypeDto,
        listOf(recipeStepDto)
    )


    @Before
    fun setup() {
        mockRecipeListDao = mock(RecipeListDao::class.java)
        recipeListLocalDataSourceImpl = RecipeListLocalDataSourceImpl(mockRecipeListDao)
    }

    @Test
    fun fetchLatestRecipeListAfter_getLatestRecipeList_true(): Unit = runBlocking {
        //given
        `when`(mockRecipeListDao.fetchLatestRecipeList(testPageSize, testIndex))
            .thenReturn(listOf(recipeDto))
        //when
        val testResult = recipeListLocalDataSourceImpl.fetchLatestRecipeListAfter(testIndex)
        //then
        assertEquals(listOf(recipeDto).map { it.toDomain() }, testResult.getOrNull())
    }

    @Test
    fun fetchFavoriteRecipeListAfter_getFavoriteRecipeList_true(): Unit = runBlocking {
        //given
        `when`(mockRecipeListDao.fetchFavoriteRecipeList(testPageSize, testIndex))
            .thenReturn(listOf(recipeDto))
        //when
        val testResult = recipeListLocalDataSourceImpl.fetchFavoriteRecipeListAfter(testIndex)
        //then
        assertEquals(listOf(recipeDto).map { it.toDomain() }, testResult.getOrNull())
    }

    @Test
    fun fetchSearchRecipeListAfter_getSearchRecipeList_true(): Unit = runBlocking {
        //given
        `when`(mockRecipeListDao.fetchSearchRecipeList(testPageSize, "%${testKeyword}%", testIndex))
            .thenReturn(listOf(recipeDto))
        //when
        val testResult = recipeListLocalDataSourceImpl.fetchSearchRecipeListAfter(testKeyword, testIndex)
        println("aaa $testResult")
        //then
        assertEquals(listOf(recipeDto).map { it.toDomain() }, testResult.getOrNull())
    }

    @Test
    fun fetchTitleListAfter_getTitleList_true(): Unit = runBlocking {
        //given
        `when`(mockRecipeListDao.fetchTitleRecipeList(testPageSize, testIndex))
            .thenReturn(listOf(recipeDto))
        //when
        val testResult = recipeListLocalDataSourceImpl.fetchTitleListAfter(testIndex)
        //then
        assertEquals(listOf(recipeDto).map { it.toDomain() }, testResult.getOrNull())
    }
}