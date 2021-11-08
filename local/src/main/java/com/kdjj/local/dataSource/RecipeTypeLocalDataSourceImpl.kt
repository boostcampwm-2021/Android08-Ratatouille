package com.kdjj.local.dataSource

import com.kdjj.data.recipetype.RecipeTypeLocalDataSource
import com.kdjj.domain.model.RecipeType
import com.kdjj.local.DAO.RecipeDAO
import com.kdjj.local.entityToDomain
import com.kdjj.local.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeTypeLocalDataSourceImpl @Inject constructor(
	private val recipeDatabase: RecipeDAO
) : RecipeTypeLocalDataSource {
	
	//Recipe Type 저장하기
	override suspend fun saveRecipeType(recipeTypeList: List<RecipeType>): Result<Boolean> =
		withContext(Dispatchers.IO) {
			try {
				recipeTypeList.map { recipeType ->
					recipeType.toEntity()
				}.forEach { recipeTypeEntity ->
					recipeDatabase.insertRecipeType(recipeTypeEntity)
				}
				Result.success(true)
			} catch (e: Exception) {
				Result.failure(Exception(e.message))
			}
		}
	
	//Recipe Type 읽어오기
	override suspend fun fetchRecipeTypes(): Result<List<RecipeType>> =
		withContext(Dispatchers.IO) {
			return@withContext try {
				val recipeTypeList = recipeDatabase.getAllRecipeTypes()
					.map { entityToDomain(it) }
				Result.success(recipeTypeList)
			} catch (e: Exception) {
				Result.failure(Exception(e.message))
			}
		}
}
