package com.kdjj.local.dataSource

import com.kdjj.domain.model.*
import com.kdjj.local.dao.RecipeDao
import com.kdjj.local.dao.UselessImageDao
import com.kdjj.local.database.RecipeDatabase
import com.kdjj.local.dto.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.*

class RecipeLocalDataSourceImplTest {

    private lateinit var mockRecipeDatabase: RecipeDatabase
    private lateinit var mockRecipeDao: RecipeDao
    private lateinit var mockUselessImageDao: UselessImageDao
    private lateinit var recipeLocalDataSourceImpl: RecipeLocalDataSourceImpl

    private val dummyRecipeStepList = listOf(
        RecipeStep(
            "stepId",
            "굽기",
            RecipeStepType.FRY,
            "description",
            "image path",
            1000,
        )
    )

    private val dummyRecipe = Recipe(
        "recipeId",
        "고기",
        RecipeType(1, "한식"),
        "stuff",
        "image path",
        dummyRecipeStepList,
        "authrId",
        100,
        false,
        1000,
        RecipeState.CREATE
    )

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

    private val dummyRecipeTypeDto = RecipeTypeDto(1L, "한식")

    private val dummyRecipeDto = RecipeDto(
        dummyRecipeMeta,
        dummyRecipeTypeDto,
        listOf(dummyRecipeStepDto)
    )

    private val dummyRecipeId = "recipeId"

    @Before
    fun setup() {
        mockRecipeDatabase = mock(RecipeDatabase::class.java)
        mockRecipeDao = mock(RecipeDao::class.java)
        mockUselessImageDao = mock(UselessImageDao::class.java)
        recipeLocalDataSourceImpl =
            RecipeLocalDataSourceImpl(mockRecipeDatabase, mockRecipeDao, mockUselessImageDao)
    }

    @Test
    fun updateFavoriteRecipe_successfullyFavoriteUpdated_true(): Unit = runBlocking {
        //given
        recipeLocalDataSourceImpl.updateRecipe(dummyRecipe)
        //then
        verify(mockRecipeDao, times(1)).updateRecipeMeta(dummyRecipe.toDto())
    }

    @Test
    fun getRecipeFlow_getRecipeFlowByRecipeId_true(): Unit = runBlocking {
        //when
        `when`(mockRecipeDao.getRecipe(dummyRecipeId)).thenReturn(flowOf(dummyRecipeDto))
        //given
        val testResult = recipeLocalDataSourceImpl.getRecipeFlow(dummyRecipeId)
        //then
        testResult.collect {
            assertEquals(dummyRecipeDto.toDomain(), it)
        }
    }

    @Test
    fun getRecipe_getRecipeByRecipeId_true(): Unit = runBlocking {
        //when
        `when`(mockRecipeDao.getRecipeDto(dummyRecipeId)).thenReturn(dummyRecipeDto)
        //given
        val testResult = recipeLocalDataSourceImpl.getRecipe(dummyRecipeId)
        //then
        assertEquals(dummyRecipeDto.toDomain(), testResult.getOrNull())
    }
}