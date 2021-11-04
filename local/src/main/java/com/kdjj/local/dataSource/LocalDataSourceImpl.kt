package com.kdjj.local.dataSource

import android.util.Log
import com.kdjj.domain.model.Recipe
import com.kdjj.local.DAO.RecipeDAO
import com.kdjj.local.domainToEntity
import com.kdjj.local.model.RecipeEntity
import com.kdjj.local.model.RecipeTypeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * 1. uri에서
 * 2. return Result<Boolean> try/catch 로 구현
 *
 * 1. uri에서 원본 이미지를 읽어와서
 * 2. 내부 저장소에 그 파일을 저장하고
 * 3. 그 uri를 가져와서 DB에 저장한다
 *
 * **/
class LocalDataSourceImpl(private val recipeDatabase: RecipeDAO) : LocalDataSource {

    override suspend fun saveRecipe(recipe: Recipe): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val recipeMetaID = recipeDatabase.insertRecipeMeta(domainToEntity(recipe))
                recipe.stepList.map { recipeStep ->
                    recipeDatabase.insertRecipeStep(domainToEntity(recipeStep, recipeMetaID))
                }
                return@withContext Result.success(true)
            } catch (e: Exception) {
                return@withContext Result.failure(Exception(e.message))
            }
        }
    }

    override suspend fun saveRecipeTypes() {
    }

    override suspend fun getRecipeTypes(): List<RecipeTypeEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext recipeDatabase.getAllRecipeTypes()
        }
    }
}