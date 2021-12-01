package com.kdjj.remote.recipe

import com.kdjj.remote.datasource.RecipeTypeRemoteDataSourceImpl
import com.kdjj.remote.dto.RecipeTypeDto
import com.kdjj.remote.dto.toDomain
import com.kdjj.remote.service.RecipeTypeService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RecipeTypeRemoteDataSourceImplTest {

    private lateinit var mockRecipeTypeService: RecipeTypeService
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
        mockRecipeTypeService = mock(RecipeTypeService::class.java)
        recipeRemoteDataSourceImpl = RecipeTypeRemoteDataSourceImpl(mockRecipeTypeService)
    }

    @Test
    fun fetchRecipeTypes_getRecipeTypeList_true(): Unit = runBlocking {
        // given
        `when`(mockRecipeTypeService.fetchRecipeTypes()).thenReturn(testRecipeTypeDtoList)

        // when
        val recipeTypeList = recipeRemoteDataSourceImpl.fetchRecipeTypeList()

        // then
        assertEquals(Result.success(testRecipeTypeList), recipeTypeList)
    }
}
