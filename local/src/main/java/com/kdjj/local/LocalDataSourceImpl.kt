package com.kdjj.local

import com.kdjj.domain.model.Recipe

class LocalDataSourceImpl: LocalDataSource {

    override suspend fun saveRecipe(recipe: Recipe): Result<Boolean> {
        TODO("Not yet implemented")
    }
}