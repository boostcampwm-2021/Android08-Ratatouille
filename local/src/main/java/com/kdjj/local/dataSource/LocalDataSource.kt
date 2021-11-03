package com.kdjj.local.dataSource

import com.kdjj.domain.model.Recipe

interface LocalDataSource {

    suspend fun saveRecipe(recipe: Recipe): Result<Boolean>
}