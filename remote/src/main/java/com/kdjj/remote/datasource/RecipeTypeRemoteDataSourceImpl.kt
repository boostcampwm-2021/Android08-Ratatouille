package com.kdjj.remote.datasource

import com.kdjj.data.datasource.RecipeTypeRemoteDataSource
import com.kdjj.domain.model.RecipeType
import com.kdjj.remote.dao.FirestoreDao
import javax.inject.Inject

internal class RecipeTypeRemoteDataSourceImpl @Inject constructor(
    private val fireStoreDao: FirestoreDao
) : RecipeTypeRemoteDataSource {
    
    override suspend fun fetchRecipeTypeList(): Result<List<RecipeType>> =
        try {
            val recipeTypeList = fireStoreDao.fetchRecipeTypes()
            if (recipeTypeList.isEmpty()) throw Exception("Can't fetch recipe type.")
            Result.success(recipeTypeList)
        } catch (e: Exception) {
            Result.failure(e)
        }
}
