package com.kdjj.remote.recipe

import com.kdjj.data.recipetype.RecipeTypeRemoteDataSource
import com.kdjj.domain.model.RecipeType
import com.kdjj.remote.FirestoreDao
import javax.inject.Inject

class RecipeTypeRemoteDataSourceImpl @Inject constructor(
	private val fireStoreDao: FirestoreDao
) : RecipeTypeRemoteDataSource {

	override suspend fun fetchRecipeTypes(): Result<List<RecipeType>> =
		try {
			val recipeTypeList = fireStoreDao.fetchRecipeTypes()
			if(recipeTypeList.isEmpty()) throw Exception("Can't fetch recipe type.")
			Result.success(recipeTypeList)
		} catch (e: Exception) {
			Result.failure(e)
		}
}
