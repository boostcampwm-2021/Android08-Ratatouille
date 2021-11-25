package com.kdjj.data.datasource

interface RecipeImageLocalDataSource {
    
    suspend fun convertToByteArray(
        uri: String
    ): Result<Pair<ByteArray, Float?>>
    
    suspend fun convertToInternalStorageUri(
        byteArray: ByteArray,
        fileName: String,
        degree: Float? = null
    ): Result<String>

    fun isUriExists(uri: String): Boolean

    suspend fun deleteUselessImages(): Result<Unit>
}
