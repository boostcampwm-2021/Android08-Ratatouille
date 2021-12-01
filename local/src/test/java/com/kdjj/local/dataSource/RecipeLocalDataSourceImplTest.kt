package com.kdjj.local.dataSource

import androidx.room.withTransaction
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

    private val testRecipeStepList = listOf(
        RecipeStep(
            "stepId",
            "굽기",
            RecipeStepType.FRY,
            "description",
            "image path",
            1000,
        )
    )

    private val testRecipe = Recipe(
        "recipeId",
        "고기",
        RecipeType(1, "한식"),
        "stuff",
        "image path",
        testRecipeStepList,
        "authrId",
        100,
        false,
        1000,
        RecipeState.CREATE
    )

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

    private val recipeTypeDto = RecipeTypeDto(1L, "한식")

    private val recipeDto = RecipeDto(
        recipeMeta,
        recipeTypeDto,
        listOf(recipeStepDto)
    )

    private val testRecipeId = "recipeId"

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
        recipeLocalDataSourceImpl.updateRecipe(testRecipe)
        //then
        verify(mockRecipeDao, times(1)).updateRecipeMeta(testRecipe.toDto())
    }

    @Test
    fun getRecipeFlow_getRecipeFlowByRecipeId_true(): Unit = runBlocking {
        //when
        `when`(mockRecipeDao.getRecipe(testRecipeId)).thenReturn(flowOf(recipeDto))
        //given
        val testResult = recipeLocalDataSourceImpl.getRecipeFlow(testRecipeId)
        //then
        testResult.collect {
            assertEquals(recipeDto.toDomain(), it)
        }
    }

    @Test
    fun getRecipe_getRecipeByRecipeId_true(): Unit = runBlocking {
        //when
        `when`(mockRecipeDao.getRecipeDto(testRecipeId)).thenReturn(recipeDto)
        //given
        val testResult = recipeLocalDataSourceImpl.getRecipe(testRecipeId)
        //then
        assertEquals(recipeDto.toDomain(), testResult.getOrNull())
    }
}