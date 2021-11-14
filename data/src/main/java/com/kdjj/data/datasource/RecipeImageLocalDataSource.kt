package com.kdjj.data.datasource

interface RecipeImageLocalDataSource {
    
    suspend fun convertToByteArray(
        uri: String
    ): Result<ByteArray>
    
    suspend fun convertToInternalStorageUri(
        byteArray: ByteArray,
        fileName: String
    ): Result<String>
}
