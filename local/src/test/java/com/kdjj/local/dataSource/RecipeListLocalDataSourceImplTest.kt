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

    private val dummyIndex = 0
    private val dummyPageSize = 10
    private val dummyKeyword = "testKeyword"

    private val dummyRecipeTypeDto = RecipeTypeDto(1L, "한식")

    private val dummyRecipeMeta = RecipeMetaDto(
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

    private val dummyRecipeStepDto = RecipeStepDto(
        "stepId",
        "삶기",
        1,
        RecipeStepType.FRY,
        "description",
        "image path",
        1000,
        "recipeId"
    )

    private val dummyRecipeDto = RecipeDto(
        dummyRecipeMeta,
        dummyRecipeTypeDto,
        listOf(dummyRecipeStepDto)
    )


    @Before
    fun setup() {
        mockRecipeListDao = mock(RecipeListDao::class.java)
        recipeListLocalDataSourceImpl = RecipeListLocalDataSourceImpl(mockRecipeListDao)
    }

    @Test
    fun fetchLatestRecipeListAfter_getLatestRecipeList_true(): Unit = runBlocking {
        //given
        `when`(mockRecipeListDao.fetchLatestRecipeList(dummyPageSize, dummyIndex))
            .thenReturn(listOf(dummyRecipeDto))
        //when
        val testResult = recipeListLocalDataSourceImpl.fetchLatestRecipeListAfter(dummyIndex)
        //then
        assertEquals(listOf(dummyRecipeDto).map { it.toDomain() }, testResult.getOrNull())
    }

    @Test
    fun fetchFavoriteRecipeListAfter_getFavoriteRecipeList_true(): Unit = runBlocking {
        //given
        `when`(mockRecipeListDao.fetchFavoriteRecipeList(dummyPageSize, dummyIndex))
            .thenReturn(listOf(dummyRecipeDto))
        //when
        val testResult = recipeListLocalDataSourceImpl.fetchFavoriteRecipeListAfter(dummyIndex)
        //then
        assertEquals(listOf(dummyRecipeDto).map { it.toDomain() }, testResult.getOrNull())
    }

    @Test
    fun fetchSearchRecipeListAfter_getSearchRecipeList_true(): Unit = runBlocking {
        //given
        `when`(mockRecipeListDao.fetchSearchRecipeList(dummyPageSize, "%${dummyKeyword}%", dummyIndex))
            .thenReturn(listOf(dummyRecipeDto))
        //when
        val testResult = recipeListLocalDataSourceImpl.fetchSearchRecipeListAfter(dummyKeyword, dummyIndex)
        //then
        assertEquals(listOf(dummyRecipeDto).map { it.toDomain() }, testResult.getOrNull())
    }

    @Test
    fun fetchTitleListAfter_getTitleList_true(): Unit = runBlocking {
        //given
        `when`(mockRecipeListDao.fetchTitleRecipeList(dummyPageSize, dummyIndex))
            .thenReturn(listOf(dummyRecipeDto))
        //when
        val testResult = recipeListLocalDataSourceImpl.fetchTitleListAfter(dummyIndex)
        //then
        assertEquals(listOf(dummyRecipeDto).map { it.toDomain() }, testResult.getOrNull())
    }
}