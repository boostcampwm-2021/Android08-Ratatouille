package com.kdjj.data.repository

import com.kdjj.data.datasource.RecipeTypeLocalDataSource
import com.kdjj.data.datasource.RecipeTypeRemoteDataSource
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.repository.RecipeTypeRepository
import javax.inject.Inject

internal class RecipeTypeRepositoryImpl @Inject constructor(
    private val localDataSource: RecipeTypeLocalDataSource,
    private val remoteDataSource: RecipeTypeRemoteDataSource
) : RecipeTypeRepository {
    
    override suspend fun fetchRemoteRecipeTypeList(): Result<List<RecipeType>> {
        return remoteDataSource.fetchRecipeTypeList()
    }
    
    override suspend fun fetchLocalRecipeTypeList(): Result<List<RecipeType>> {
        return localDataSource.fetchRecipeTypeList()
    }
    
    override suspend fun saveRecipeTypeList(
        recipeTypeList: List<RecipeType>
    ): Result<Boolean> {
        return localDataSource.saveRecipeTypeList(recipeTypeList)
    }
}
