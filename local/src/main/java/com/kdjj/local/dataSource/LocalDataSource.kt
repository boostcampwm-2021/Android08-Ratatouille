package com.kdjj.local.dataSource

import com.kdjj.domain.model.Recipe
import com.kdjj.local.model.RecipeTypeEntity

interface LocalDataSource {

    suspend fun saveRecipe(recipe: Recipe): Result<Boolean>

    suspend fun saveRecipeTypes()

    suspend fun getRecipeTypes(): Result<List<RecipeTypeEntity>>

    suspend fun localUriToByteArray(uri: String): Result<ByteArray>

    suspend fun byteArrayToLocalUri(byteArray: ByteArray, fileName: String): Result<String>
}