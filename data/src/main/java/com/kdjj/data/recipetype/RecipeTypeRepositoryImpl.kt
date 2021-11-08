package com.kdjj.data.recipetype

import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.repository.RecipeTypeRepository
import javax.inject.Inject

class RecipeTypeRepositoryImpl @Inject constructor(
	private val localDataSource: RecipeTypeLocalDataSource,
	private val remoteDataSource: RecipeTypeRemoteDataSource
) : RecipeTypeRepository {
	
	override suspend fun fetchRecipeTypes(): Result<List<RecipeType>> {
		return remoteDataSource.fetchRecipeTypes()
	}
	
	override suspend fun saveRecipeType(): Result<Boolean> {
		val recipeTypeList = remoteDataSource.fetchRecipeTypes().getOrThrow()
		return localDataSource.saveRecipeType(recipeTypeList)
	}
	
	override suspend fun getRecipeTypes(): Result<List<RecipeType>> {
		return localDataSource.getRecipeTypes()
	}
}
