package com.kdjj.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kdjj.local.dto.RecipeTypeEntity

@Dao
internal interface RecipeTypeDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeType(recipeType: RecipeTypeEntity)
    
    @Query("SELECT * FROM RecipeType")
    suspend fun getAllRecipeTypeList(): List<RecipeTypeEntity>
}
