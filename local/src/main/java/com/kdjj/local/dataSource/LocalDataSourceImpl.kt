package com.kdjj.local.dataSource

import android.util.Log
import com.kdjj.domain.model.Recipe
import com.kdjj.local.DAO.RecipeDAO
import com.kdjj.local.domainToEntity
import com.kdjj.local.model.RecipeEntity
import com.kdjj.local.model.RecipeTypeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSourceImpl(private val recipeDatabase: RecipeDAO) : LocalDataSource {

    override suspend fun saveRecipe(recipe: Recipe) {
        withContext(Dispatchers.IO) {
            val recipeMetaID = recipeDatabase.insertRecipeMeta(domainToEntity(recipe))
            recipe.stepList.map { recipeStep ->
                recipeDatabase.insertRecipeStep(domainToEntity(recipeStep, recipeMetaID))
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