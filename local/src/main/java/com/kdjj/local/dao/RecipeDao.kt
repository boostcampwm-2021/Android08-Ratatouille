package com.kdjj.local.dao

import androidx.room.*
import com.kdjj.local.model.RecipeEntity
import com.kdjj.local.model.RecipeMetaEntity
import com.kdjj.local.model.RecipeStepEntity

@Dao
interface RecipeDao {
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertRecipeMeta(recipeMeta: RecipeMetaEntity)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertRecipeStep(recipeStep: RecipeStepEntity)
	
	@Transaction
	@Query("SELECT * FROM RecipeMeta")
	suspend fun getAllRecipe(): List<RecipeEntity>
}
