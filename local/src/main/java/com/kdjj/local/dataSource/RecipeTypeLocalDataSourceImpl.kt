package com.kdjj.local.dataSource

import com.kdjj.data.common.errorMap
import com.kdjj.data.datasource.RecipeTypeLocalDataSource
import com.kdjj.domain.model.RecipeType
import com.kdjj.local.dao.RecipeTypeDao
import com.kdjj.local.dto.toDomain
import com.kdjj.local.dto.toDto
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
            runCatching {
                recipeTypeList.map { recipeType ->
                    recipeType.toDto()
                }.forEach { recipeTypeEntity ->
                    recipeTypeDao.insertRecipeType(recipeTypeEntity)
                }
                true
            }.errorMap {
                Exception(it.message)
            }
        }
    
    override suspend fun fetchRecipeTypeList(): Result<List<RecipeType>> =
        withContext(Dispatchers.IO) {
            runCatching {
                recipeTypeDao.getAllRecipeTypeList()
                    .map { it.toDomain() }
            }.errorMap {
                Exception(it.message)
            }
        }
}
