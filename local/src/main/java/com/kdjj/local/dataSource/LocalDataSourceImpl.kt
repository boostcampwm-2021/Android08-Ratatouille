package com.kdjj.local.dataSource

import com.kdjj.domain.model.Recipe
import com.kdjj.local.DAO.RecipeDAO
import com.kdjj.local.FileSaveHelper
import com.kdjj.local.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSourceImpl(
	private val recipeDatabase: RecipeDAO,
	private val fileSaveHelper: FileSaveHelper
) : LocalDataSource {
	
	//Recipe 저장
	override suspend fun saveRecipe(recipe: Recipe): Result<Boolean> =
		withContext(Dispatchers.IO) {
			try {
				recipeDatabase.insertRecipeMeta(recipe.toEntity())
				recipe.stepList.forEachIndexed { idx, recipeStep ->
					recipeDatabase.insertRecipeStep(
						recipeStep.toEntity(recipe.recipeId, idx + 1)
					)
				}
				Result.success(true)
			} catch (e: Exception) {
				Result.failure(Exception(e.message))
			}
		}
	
	//Gallery Image Uri를 BtyeArray로 변환
	override suspend fun localUriToByteArray(uri: String): Result<ByteArray> =
		fileSaveHelper.convertToByteArray(uri)
	
	//Image ByteArray를 Internal 저장소에 저장후 imagePath(파일 경로 반환) fileName == recipe or step ID
	override suspend fun byteArrayToLocalUri(
		byteArray: ByteArray,
		fileName: String
	): Result<String> =
		fileSaveHelper.convertToInternalStorageUri(byteArray, fileName)
}
