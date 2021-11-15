package com.kdjj.local.dao

import androidx.room.*
import com.kdjj.domain.model.Recipe
import com.kdjj.local.dto.RecipeDto
import com.kdjj.local.dto.RecipeMetaDto
import com.kdjj.local.dto.RecipeStepDto
import com.kdjj.local.dto.toDto

@Dao
internal interface RecipeDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeMeta(recipeMeta: RecipeMetaDto)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeStep(recipeStep: RecipeStepDto)
    
    @Transaction
    suspend fun insertRecipe(recipe: Recipe) {
        insertRecipeMeta(recipe.toDto())
        recipe.stepList.forEachIndexed { idx, recipeStep ->
            insertRecipeStep(recipeStep.toDto(recipe.recipeId, idx + 1))
        }
    }
    
    @Transaction
    @Query("SELECT * FROM RecipeMeta")
    suspend fun getAllRecipe(): List<RecipeDto>
    
    @Query("DELETE FROM RecipeStep WHERE parentRecipeId = :recipeId")
    suspend fun deleteStepList(recipeId: String)
    
    @Delete
    suspend fun deleteRecipe(recipeMeta: RecipeMetaDto)
    
    @Update
    suspend fun updateRecipeMeta(recipeMeta: RecipeMetaDto)
}
