package com.kdjj.local.dataSource

import com.kdjj.domain.model.RecipeType
import com.kdjj.local.dao.RecipeTypeDao
import com.kdjj.local.dto.toDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class RecipeTypeLocalDataSourceImplTest {

    private lateinit var mockRecipeTypeDao: RecipeTypeDao
    private lateinit var recipeTypeLocalDataSourceImpl: RecipeTypeLocalDataSourceImpl

    private val dummyRecipeTypeList = listOf(
        RecipeType(1, "a"),
        RecipeType(2, "b"),
        RecipeType(3, "c"),
        RecipeType(4, "d")
    )

    private val dummyRecipeTypeDtoList = dummyRecipeTypeList.map { it.toDto() }

    @Before
    fun setup() {
        mockRecipeTypeDao = mock(RecipeTypeDao::class.java)
        recipeTypeLocalDataSourceImpl = RecipeTypeLocalDataSourceImpl(mockRecipeTypeDao)
    }

    @Test
    fun saveRecipeTypeList_saveRecipeTypeListIntoDatabase_true(): Unit = runBlocking {
        //given
        recipeTypeLocalDataSourceImpl.saveRecipeTypeList(dummyRecipeTypeList)
        //then
        dummyRecipeTypeList.forEach {
            verify(mockRecipeTypeDao, times(1)).insertRecipeType(it.toDto())
        }
    }

    @Test
    fun fetchRecipeTypeList_getRecipeTypes_true(): Unit = runBlocking {
        //when
        `when`(mockRecipeTypeDao.getAllRecipeTypeList()).thenReturn(dummyRecipeTypeDtoList)
        //given
        val testResult = recipeTypeLocalDataSourceImpl.fetchRecipeTypeList()
        //then
        assertEquals(dummyRecipeTypeList, testResult.getOrNull())
    }
}
