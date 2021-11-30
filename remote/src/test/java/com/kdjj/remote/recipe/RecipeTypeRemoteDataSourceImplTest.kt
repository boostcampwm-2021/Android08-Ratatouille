package com.kdjj.remote.recipe

import com.kdjj.remote.datasource.RecipeTypeRemoteDataSourceImpl
import com.kdjj.remote.dto.RecipeTypeDto
import com.kdjj.remote.dto.toDomain
import com.kdjj.remote.service.RecipeTypeServiceImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RecipeTypeRemoteDataSourceImplTest {

    private lateinit var mockRecipeTypeServiceImpl: RecipeTypeServiceImpl
    private lateinit var recipeRemoteDataSourceImpl: RecipeTypeRemoteDataSourceImpl
    private val testRecipeTypeDtoList = listOf(
        RecipeTypeDto(1, "한식"),
        RecipeTypeDto(1, "중식"),
        RecipeTypeDto(1, "양식")
    )
    private val testRecipeTypeList = testRecipeTypeDtoList.map { it.toDomain() }

    @Before
    fun setup() {
        // given
        mockRecipeTypeServiceImpl = mock(RecipeTypeServiceImpl::class.java)
        recipeRemoteDataSourceImpl = RecipeTypeRemoteDataSourceImpl(mockRecipeTypeServiceImpl)
    }

    @Test
    fun fetchRecipeTypes_getRecipeTypeList_true(): Unit = runBlocking {
        // given
        `when`(mockRecipeTypeServiceImpl.fetchRecipeTypes()).thenReturn(testRecipeTypeDtoList)

        // when
        val recipeTypeList = recipeRemoteDataSourceImpl.fetchRecipeTypeList()

        // then
        assertEquals(Result.success(testRecipeTypeList), recipeTypeList)
    }
}
