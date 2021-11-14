package com.kdjj.local.dataSource

import com.kdjj.data.datasource.RecipeTypeLocalDataSource
import com.kdjj.domain.model.RecipeType
import com.kdjj.local.dao.RecipeTypeDao
import com.kdjj.local.dto.toDomain
import com.kdjj.local.dto.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RecipeTypeLocalDataSourceImpl @Inject constructor(
    private val recipeTypeDao: RecipeTypeDao
) : RecipeTypeLocalDataSource {
    
    override suspend fun saveRecipeTypeList(
        recipeTypeList: List<RecipeType>
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                recipeTypeList.map { recipeType ->
                    recipeType.toEntity()
                }.forEach { recipeTypeEntity ->
                    recipeTypeDao.insertRecipeType(recipeTypeEntity)
                }
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception(e.message))
            }
        }
    
    override suspend fun fetchRecipeTypeList(): Result<List<RecipeType>> =
        withContext(Dispatchers.IO) {
            try {
                val recipeTypeList = recipeTypeDao.getAllRecipeTypeList()
                    .map { it.toDomain() }
                Result.success(recipeTypeList)
            } catch (e: Exception) {
                Result.failure(Exception(e.message))
            }
        }
}
