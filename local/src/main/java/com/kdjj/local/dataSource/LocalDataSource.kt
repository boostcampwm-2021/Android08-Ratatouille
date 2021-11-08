package com.kdjj.local.dataSource

import com.kdjj.domain.model.Recipe

interface LocalDataSource {
	
	suspend fun saveRecipe(recipe: Recipe): Result<Boolean>
	
	suspend fun localUriToByteArray(uri: String): Result<ByteArray>
	
	suspend fun byteArrayToLocalUri(byteArray: ByteArray, fileName: String): Result<String>
}