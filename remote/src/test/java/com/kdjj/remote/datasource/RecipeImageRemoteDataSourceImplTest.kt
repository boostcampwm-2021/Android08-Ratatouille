package com.kdjj.remote.datasource

//class RecipeImageRemoteDataSourceImplTest {
//
//	private lateinit var mockFireStorageDaoImpl: FirebaseStorageServiceImpl
//	private lateinit var recipeImageRemoteDataSourceImpl: RecipeImageRemoteDataSourceImpl
//	private val testUriList = listOf("this is test uri")
//	private val testByteArrayList = listOf(testUriList.first().toByteArray())
//
//	@Before
//	fun setup() {
//		mockFireStorageDaoImpl = mock(FirebaseStorageServiceImpl::class.java)
//		recipeImageRemoteDataSourceImpl = RecipeImageRemoteDataSourceImpl(mockFireStorageDaoImpl)
//	}
//
//	@Test
//	fun fetchRecipeImage_getImageByteArray_true(): Unit = runBlocking {
//		//given
//		`when`(mockFireStorageDaoImpl.fetchRecipeImage(testUriList)).thenReturn(
//			Result.success(
//				testByteArrayList
//			)
//		)
//		//when
//		val byteArrayResult = recipeImageRemoteDataSourceImpl.fetchRecipeImage(testUriList)
//		//then
//		assertEquals(testByteArrayList, byteArrayResult.getOrNull())
//	}
//
//	@Test
//	fun uploadRecipeImage_getRemoteStorageImageUri_true(): Unit = runBlocking {
//		//given
//		`when`(mockFireStorageDaoImpl.uploadRecipeImage(testUriList.first())).thenReturn(Result.success(testUriList.first()))
//		//when
//		val newImagePathResult = recipeImageRemoteDataSourceImpl.uploadRecipeImage(testUriList.first())
//		//then
//		assertEquals(testUriList, newImagePathResult.getOrNull())
//	}
//}
