package com.kdjj.data.datasource

interface RecipeImageRemoteDataSource {
    
    suspend fun fetchRecipeImage(
        uri: String
    ): Result<ByteArray>
    
    suspend fun uploadRecipeImage(
        uri: String
    ): Result<String>
}
