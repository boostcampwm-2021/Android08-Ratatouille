package com.kdjj.local.dataSource

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.kdjj.domain.model.Recipe
import com.kdjj.local.DAO.RecipeDAO
import com.kdjj.local.FileSaveHelper
import com.kdjj.local.domainToEntity
import com.kdjj.local.model.RecipeTypeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

/**
 * 1. uri에서
 * 2. return Result<Boolean> try/catch 로 구현
 *
 * 1. uri에서 원본 이미지를 읽어와서
 * 2. 내부 저장소에 그 파일을 저장하고
 * 3. 그 uri를 가져와서 DB에 저장한다
 *
 * InputStream으로 이미지 불러와서 -> ByteStream으로 변환해서 -> Internal Storage 저장
 *
 * **/
class LocalDataSourceImpl(
    private val recipeDatabase: RecipeDAO,
    private val fileSaveHelper: FileSaveHelper
) :
    LocalDataSource {

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
}