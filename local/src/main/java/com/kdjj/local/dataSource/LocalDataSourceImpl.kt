package com.kdjj.local.dataSource

import com.kdjj.domain.model.Recipe
import com.kdjj.local.dataSource.LocalDataSource

class LocalDataSourceImpl: LocalDataSource {

    override suspend fun saveRecipe(recipe: Recipe): Result<Boolean> {
        TODO("Not yet implemented")
    }
}