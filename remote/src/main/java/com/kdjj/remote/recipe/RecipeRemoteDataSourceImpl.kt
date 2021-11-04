package com.kdjj.remote.recipe

import com.kdjj.domain.model.RecipeType
import com.kdjj.remote.FirestoreDao

class RecipeRemoteDataSourceImpl(
	private val fireStoreDao: FirestoreDao
) : RecipeRemoteDataSource {

	override suspend fun fetchRecipeTypes(): Result<List<RecipeType>> =
		try {
			val recipeTypeList = fireStoreDao.fetchRecipeTypes()
			Result.success(recipeTypeList)
		} catch (e: Exception) {
			Result.failure(e)
		}
}