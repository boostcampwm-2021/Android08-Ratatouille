package com.kdjj.local.dao

import androidx.room.*
import com.kdjj.local.dto.RecipeDto
import com.kdjj.local.dto.RecipeMetaDto
import com.kdjj.local.dto.RecipeStepDto
import kotlinx.coroutines.flow.Flow

@Dao
internal interface RecipeDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeMeta(recipeMeta: RecipeMetaDto)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeStep(recipeStep: RecipeStepDto)
    
    @Query("DELETE FROM RecipeStep WHERE parentRecipeId = :recipeId")
    suspend fun deleteStepList(recipeId: String)
    
    @Delete
    suspend fun deleteRecipe(recipeMeta: RecipeMetaDto)
    
    @Update
    suspend fun updateRecipeMeta(recipeMeta: RecipeMetaDto)
    
    @Query("SELECT * FROM RecipeMeta WHERE recipeMetaId = :recipeId")
    fun getRecipe(recipeId: String): Flow<RecipeDto>

    @Query("SELECT * FROM RecipeMeta WHERE recipeMetaId = :recipeId")
    fun getRecipeDto(recipeId: String): RecipeDto
}
