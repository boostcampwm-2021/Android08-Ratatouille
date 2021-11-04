package com.kdjj.local.dataSource

import com.kdjj.domain.model.Recipe
import com.kdjj.local.DAO.RecipeDAO
import com.kdjj.local.FileSaveHelper
import com.kdjj.local.domainToEntity
import com.kdjj.local.model.RecipeTypeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class LocalDataSourceImpl(
    private val recipeDatabase: RecipeDAO,
    private val fileSaveHelper: FileSaveHelper
) : LocalDataSource {

    //Recipe 저장
    override suspend fun saveRecipe(recipe: Recipe): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                recipeDatabase.insertRecipeMeta(domainToEntity(recipe))
                recipe.stepList.forEachIndexed { idx, recipeStep ->
                    recipeDatabase.insertRecipeStep(
                        domainToEntity(
                            recipeStep,
                            recipe.recipeId,
                            idx + 1
                        )
                    )
                }
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception(e.message))
            }
        }
    }

    //Recipe Type 저장하기
    override suspend fun saveRecipeTypes() {
    }

    //Recipe Type 읽어오기
    override suspend fun getRecipeTypes(): Result<List<RecipeTypeEntity>> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val recipeTypeList = recipeDatabase.getAllRecipeTypes()
                Result.success(recipeTypeList)
            } catch (e: Exception) {
                Result.failure(Exception(e.message))
            }
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

    /**
     * 추가로 구현해야하는 함수(Firebase에서 훔쳐온 레시피):
     * fun remoteUriToByteArray(str: String): ByteArray
     * fun byteArrayToRemoteUri(array: ByteArray): String
     * */
}