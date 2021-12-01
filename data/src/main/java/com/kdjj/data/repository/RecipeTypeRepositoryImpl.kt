package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeTypeLocalDataSource
import com.kdjj.data.datasource.RecipeTypeRemoteDataSource
import com.kdjj.domain.common.handleWith
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.repository.RecipeTypeRepository
import javax.inject.Inject

internal class RecipeTypeRepositoryImpl @Inject constructor(
    private val localDataSource: RecipeTypeLocalDataSource,
    private val remoteDataSource: RecipeTypeRemoteDataSource
) : RecipeTypeRepository {
    
    override suspend fun fetchRecipeTypeList(): Result<List<RecipeType>> {
        return remoteDataSource.fetchRecipeTypeList().onSuccess { result ->
            localDataSource.saveRecipeTypeList(result)
        }.handleWith {
            localDataSource.fetchRecipeTypeList()
        }
    }
}
