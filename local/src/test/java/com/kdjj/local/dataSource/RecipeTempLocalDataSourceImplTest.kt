package com.kdjj.local.dataSource

import org.junit.Assert.*
import com.kdjj.domain.model.*
import com.kdjj.local.dao.RecipeTempDao
import com.kdjj.local.dao.UselessImageDao
import com.kdjj.local.database.RecipeDatabase
import com.kdjj.local.dto.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class RecipeTempLocalDataSourceImplTest {

    private lateinit var mockRecipeDatabase: RecipeDatabase
    private lateinit var mockRecipeTempDao: RecipeTempDao
    private lateinit var mockUselessImageDao: UselessImageDao
    private lateinit var recipeTempLocalDataSourceImpl: RecipeTempLocalDataSourceImpl

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

    private val dummyRecipeTypeDto = RecipeTypeDto(1L, "한식")

    private val dummyRecipeMeta = RecipeTempMetaDto(
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

    private val dummyRecipeStepDto = RecipeTempStepDto(
        "stepId",
        "삶기",
        1,
        RecipeStepType.FRY,
        "description",
        "image path",
        1000,
        "recipeId"
    )

    private val dummyRecipeTempDto = RecipeTempDto(
        dummyRecipeMeta,
        dummyRecipeTypeDto,
        listOf(dummyRecipeStepDto)
    )


    private val dummyId = "testId"

    @Before
    fun setup() {
        mockRecipeDatabase = mock(RecipeDatabase::class.java)
        mockRecipeTempDao = mock(RecipeTempDao::class.java)
        mockUselessImageDao = mock(UselessImageDao::class.java)
        recipeTempLocalDataSourceImpl = RecipeTempLocalDataSourceImpl(
            mockRecipeTempDao,
            mockRecipeDatabase,
            mockUselessImageDao
        )
    }

    @Test
    fun getRecipeTemp_getRecipe_true(): Unit = runBlocking {
        //given
        `when`(mockRecipeTempDao.getRecipeTemp(dummyId)).thenReturn(dummyRecipeTempDto)
        //when
        val testResult = recipeTempLocalDataSourceImpl.getRecipeTemp(dummyId)
        //then
        assertEquals(dummyRecipeTempDto.toDomain(), testResult.getOrNull())
    }
}