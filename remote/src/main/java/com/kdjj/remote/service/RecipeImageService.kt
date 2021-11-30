package com.kdjj.remote.service

internal interface RecipeImageService {

    suspend fun fetchRecipeImage(
        uriList: List<String>
    ): List<ByteArray>

    suspend fun uploadRecipeImage(
        uri: String
    ): String
}
