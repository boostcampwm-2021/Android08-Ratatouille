package com.kdjj.data.recipetype

import com.kdjj.domain.repository.RecipeTypeRepository

class RecipeTypeRepositoryImpl(
	private val localDataSource: RecipeTypeLocalDataSource,
	private val remoteDataSource: RecipeTypeRemoteDataSource
) : RecipeTypeRepository {
}