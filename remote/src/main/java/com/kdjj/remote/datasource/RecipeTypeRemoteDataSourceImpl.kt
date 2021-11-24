package com.kdjj.remote.datasource

import com.kdjj.data.common.errorMap
import com.kdjj.data.datasource.RecipeTypeRemoteDataSource
import com.kdjj.domain.model.RecipeType
import com.kdjj.remote.dto.toDomain
import com.kdjj.remote.service.FirestoreService
import javax.inject.Inject

internal class RecipeTypeRemoteDataSourceImpl @Inject constructor(
    private val fireStoreService: FirestoreService
) : RecipeTypeRemoteDataSource {

    override suspend fun fetchRecipeTypeList(): Result<List<RecipeType>> =
        runCatching {
            val recipeTypeList = fireStoreService.fetchRecipeTypes()
                .map { it.toDomain() }
            if (recipeTypeList.isEmpty()) throw Exception("Can't fetch recipe type.")
            recipeTypeList
        }.errorMap {
            Exception(it.message)
        }
}
