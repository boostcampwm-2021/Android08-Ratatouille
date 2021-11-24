package com.kdjj.local.dataSource

import com.kdjj.local.ImageFileHelper
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RecipeImageLocalDataSourceImplTest {
	
	private lateinit var mockImageFileHelper: ImageFileHelper
	private lateinit var recipeImageLocalDataSourceImpl: RecipeImageLocalDataSourceImpl
	private val testUri = "this is test uri"
	private val testByteArray = testUri.toByteArray()
	private val testDegree = 90f
	private val testPair = Pair(testByteArray, testDegree)

	@Before
	fun setup() {
		mockImageFileHelper = mock(ImageFileHelper::class.java)
		recipeImageLocalDataSourceImpl = RecipeImageLocalDataSourceImpl(mockImageFileHelper)
	}

	@Test
	fun convertToByteArray_getImageByteArrayAndImageDegree_true(): Unit = runBlocking {
		//given
		`when`(recipeImageLocalDataSourceImpl.convertToByteArray(testUri)).thenReturn(
			Result.success(testByteArray to testDegree)
		)
		//when
		val result = recipeImageLocalDataSourceImpl.convertToByteArray(testUri)
		//then
		assertEquals(testPair, result.getOrNull())
	}

	@Test
	fun convertToInternalStorageUri_localStorageImageUri_true(): Unit = runBlocking {
		//given
		`when`(
			recipeImageLocalDataSourceImpl.convertToInternalStorageUri(
				testByteArray,
				"fileName",
				testDegree
			)
		).thenReturn(Result.success(testUri))
		//when
		val newLocalImagePathResult =
			recipeImageLocalDataSourceImpl.convertToInternalStorageUri(testByteArray, "fileName", testDegree)
		//then
		assertEquals(testUri, newLocalImagePathResult.getOrNull())
	}
}
