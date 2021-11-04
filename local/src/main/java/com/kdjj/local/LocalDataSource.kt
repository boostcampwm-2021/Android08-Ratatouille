package com.kdjj.local

import com.kdjj.domain.model.Recipe

interface LocalDataSource {

    suspend fun saveRecipe(recipe: Recipe): Result<Boolean>
}