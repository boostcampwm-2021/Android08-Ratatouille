package com.kdjj.data.datasource

interface RecipeImageRemoteDataSource {
    
    suspend fun fetchRecipeImage(
        uriList: List<String>
    ): Result<List<ByteArray>>
    
    suspend fun uploadRecipeImage(
        uri: String
    ): Result<String>
}
