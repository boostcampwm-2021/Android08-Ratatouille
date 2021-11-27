package com.kdjj.local.dao

import androidx.room.*
import com.kdjj.domain.model.Recipe
import com.kdjj.local.dto.*

@Dao
internal interface RecipeTempDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeTempMeta(recipeTempMeta: RecipeTempMetaDto)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeTempStep(recipeTempStep: RecipeTempStepDto)

    @Query("DELETE FROM RecipeTempStep WHERE parentRecipeId = :recipeId")
    suspend fun deleteTempStepList(recipeId: String)

    @Query("DELETE FROM RecipeTempMeta WHERE recipeMetaId = :recipeId")
    suspend fun deleteRecipeTemp(recipeId: String)
    
    @Query("SELECT * FROM RecipeTempMeta WHERE recipeMetaId = :recipeId")
    fun getRecipeTemp(recipeId: String): RecipeTempDto?
}
