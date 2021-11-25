package com.kdjj.data.datasource

interface RecipeImageLocalDataSource {
    
    suspend fun convertToByteArray(
        uriList: List<String>
    ): Result<List<Pair<ByteArray, Float?>>>
    
    suspend fun convertToInternalStorageUri(
        byteArrayList: List<ByteArray>,
        fileNameList: List<String>,
        degreeList: List<Float?>
    ): Result<List<String>>

    fun isUriExists(uri: String): Boolean

    suspend fun deleteUselessImages(): Result<Unit>
}
