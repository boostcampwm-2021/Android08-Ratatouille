package com.kdjj.local.dao

import androidx.room.*
import com.kdjj.domain.model.Recipe
import com.kdjj.local.dto.RecipeEntity
import com.kdjj.local.dto.RecipeMetaEntity
import com.kdjj.local.dto.RecipeStepEntity
import com.kdjj.local.dto.toEntity

@Dao
internal interface RecipeDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeMeta(recipeMeta: RecipeMetaEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeStep(recipeStep: RecipeStepEntity)
    
    @Transaction
    suspend fun insertRecipe(recipe: Recipe) {
        insertRecipeMeta(recipe.toEntity())
        recipe.stepList.forEachIndexed { idx, recipeStep ->
            insertRecipeStep(recipeStep.toEntity(recipe.recipeId, idx + 1))
        }
    }
    
    @Transaction
    @Query("SELECT * FROM RecipeMeta")
    suspend fun getAllRecipe(): List<RecipeEntity>
    
    @Query("DELETE FROM RecipeStep WHERE parentRecipeId = :recipeId")
    suspend fun deleteStepList(recipeId: String)
    
    @Delete
    suspend fun deleteRecipe(recipeMeta: RecipeMetaEntity)
    
    @Update
    suspend fun updateRecipeMeta(recipeMeta: RecipeMetaEntity)
}
