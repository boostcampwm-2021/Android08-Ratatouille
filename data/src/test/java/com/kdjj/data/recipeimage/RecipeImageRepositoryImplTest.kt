package com.kdjj.data.recipeimage

import com.kdjj.data.datasource.RecipeImageLocalDataSource
import com.kdjj.data.datasource.RecipeImageRemoteDataSource
import com.kdjj.data.repository.RecipeImageRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RecipeImageRepositoryImplTest {
	
	private lateinit var mockRecipeImageLocalDataSource: RecipeImageLocalDataSource
	private lateinit var mockRecipeImageRemoteDataSource: RecipeImageRemoteDataSource
	private lateinit var recipeImageRepositoryImpl: RecipeImageRepositoryImpl
	private val testUri = "this is test uri"
	private val testByteArray = testUri.toByteArray()
	
	@Before
	fun setup() {
		mockRecipeImageLocalDataSource = mock(RecipeImageLocalDataSource::class.java)
		mockRecipeImageRemoteDataSource = mock(RecipeImageRemoteDataSource::class.java)
		recipeImageRepositoryImpl = RecipeImageRepositoryImpl(
			mockRecipeImageLocalDataSource,
			mockRecipeImageRemoteDataSource
		)
	}
	
	@Test
	fun convertToByteArrayRemote_getRemoteImageByteArray_true(): Unit = runBlocking {
		//given
		`when`(mockRecipeImageRemoteDataSource.fetchRecipeImage(testUri)).thenReturn(
			Result.success(
				testByteArray
			)
		)
		//when
		val byteArrayResult = recipeImageRepositoryImpl.convertRemoteUriToByteArray(testUri)
		//then
		assertEquals(testByteArray, byteArrayResult.getOrNull())
	}
	
	@Test
	fun convertToByteArrayLocal_getLocalImageByteArray_true(): Unit = runBlocking {
		//given
		`when`(mockRecipeImageLocalDataSource.convertToByteArray(testUri)).thenReturn(
			Result.success(
				testByteArray
			)
		)
		//when
		val byteArrayResult = recipeImageRepositoryImpl.convertLocalUriToByteArray(testUri)
		//then
		assertEquals(testByteArray, byteArrayResult.getOrNull())
	}
	
	@Test
	fun convertToRemoteStorageUri_getRemoteImageUri_true(): Unit = runBlocking {
		//given
		`when`(mockRecipeImageRemoteDataSource.uploadRecipeImage(testUri)).thenReturn(
			Result.success(
				testUri
			)
		)
		//when
		val newImagePathResult = recipeImageRepositoryImpl.convertLocalUriToRemoteStorageUri(testUri)
		//then
		assertEquals(testUri, newImagePathResult.getOrNull())
	}
	
	@Test
	fun convertToLocalStorageUri_getLocalImageUri_true(): Unit = runBlocking {
		//given
		`when`(
			mockRecipeImageLocalDataSource.convertToInternalStorageUri(
				testByteArray,
				"fileName"
			)
		).thenReturn(Result.success(testUri))
		//when
		val newImagePathResult =
			recipeImageRepositoryImpl.convertByteArrayToLocalStorageUri(testByteArray, "fileName")
		//then
		assertEquals(testUri, newImagePathResult.getOrNull())
	}
}
