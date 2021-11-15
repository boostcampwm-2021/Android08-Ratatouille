package com.kdjj.remote.recipe

import com.kdjj.remote.dao.FirebaseStorageServiceImpl
import com.kdjj.remote.datasource.RecipeImageRemoteDataSourceImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RecipeImageRemoteDataSourceImplTest {
	
	private lateinit var mockFireStorageDaoImpl: FirebaseStorageServiceImpl
	private lateinit var recipeImageRemoteDataSourceImpl: RecipeImageRemoteDataSourceImpl
	private val testUri = "this is test uri"
	private val testByteArray = testUri.toByteArray()
	
	@Before
	fun setup() {
		mockFireStorageDaoImpl = mock(FirebaseStorageServiceImpl::class.java)
		recipeImageRemoteDataSourceImpl = RecipeImageRemoteDataSourceImpl(mockFireStorageDaoImpl)
	}
	
	@Test
	fun fetchRecipeImage_getImageByteArray_true(): Unit = runBlocking {
		//given
		`when`(mockFireStorageDaoImpl.fetchRecipeImage(testUri)).thenReturn(
			Result.success(
				testByteArray
			)
		)
		//when
		val byteArrayResult = recipeImageRemoteDataSourceImpl.fetchRecipeImage(testUri)
		//then
		assertEquals(testByteArray, byteArrayResult.getOrNull())
	}
	
	@Test
	fun uploadRecipeImage_getRemoteStorageImageUri_true(): Unit = runBlocking {
		//given
		`when`(mockFireStorageDaoImpl.uploadRecipeImage(testUri)).thenReturn(Result.success(testUri))
		//when
		val newImagePathResult = recipeImageRemoteDataSourceImpl.uploadRecipeImage(testUri)
		//then
		assertEquals(testUri, newImagePathResult.getOrNull())
	}
}
