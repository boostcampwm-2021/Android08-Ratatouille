package com.kdjj.data.recipeimage

interface RecipeImageRemoteDataSource {

    suspend fun fetchRecipeImage(uri: String): Result<ByteArray>
}