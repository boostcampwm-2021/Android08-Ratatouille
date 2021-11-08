package com.kdjj.local.dataSource

import com.kdjj.data.recipetype.RecipeTypeLocalDataSource
import com.kdjj.domain.model.RecipeType
import com.kdjj.local.DAO.RecipeDAO
import com.kdjj.local.FileSaveHelper
import com.kdjj.local.entityToDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeTypeLocalDataSourceImpl(
	private val recipeDatabase: RecipeDAO,
	private val fileSaveHelper: FileSaveHelper
) : RecipeTypeLocalDataSource {
	
	//Recipe Type 저장하기
	override suspend fun saveRecipeType(): Result<Boolean> {
		TODO("Not yet implemented")
	}
	
	//Recipe Type 읽어오기
	override suspend fun getRecipeTypes(): Result<List<RecipeType>> =
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