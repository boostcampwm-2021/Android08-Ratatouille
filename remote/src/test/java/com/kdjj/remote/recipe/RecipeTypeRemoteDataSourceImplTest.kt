package com.kdjj.remote.recipe

import com.kdjj.domain.model.RecipeType
import com.kdjj.remote.FirestoreDaoImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RecipeTypeRemoteDataSourceImplTest {
	
	private lateinit var mockFireStoreDaoImpl: FirestoreDaoImpl
	private lateinit var recipeRemoteDataSourceImpl: RecipeTypeRemoteDataSourceImpl
	private val testRecipeTypeList = listOf(
		RecipeType(1, "한식"),
		RecipeType(1, "중식"),
		RecipeType(1, "양식")
	)
	
	@Before
	fun setup() {
		// given
		mockFireStoreDaoImpl = mock(FirestoreDaoImpl::class.java)
		recipeRemoteDataSourceImpl = RecipeTypeRemoteDataSourceImpl(mockFireStoreDaoImpl)
	}
	
	@Test
	fun fetchRecipeTypes_getRecipeTypeList_true(): Unit = runBlocking {
		// given
		`when`(mockFireStoreDaoImpl.fetchRecipeTypes()).thenReturn(testRecipeTypeList)
		
		// when
		val recipeTypeList = recipeRemoteDataSourceImpl.fetchRecipeTypeList()
		println(recipeTypeList)
		// then
		assertEquals(Result.success(testRecipeTypeList), recipeTypeList)
	}
}
