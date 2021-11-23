package com.kdjj.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kdjj.local.dao.ImageValidationDao
import com.kdjj.local.dao.RecipeDao
import com.kdjj.local.dao.RecipeListDao
import com.kdjj.local.dao.RecipeTypeDao
import com.kdjj.local.dto.RecipeMetaDto
import com.kdjj.local.dto.RecipeStepDto
import com.kdjj.local.dto.RecipeTypeDto

@Database(
    entities = [RecipeMetaDto::class, RecipeTypeDto::class, RecipeStepDto::class],
    version = 1,
    exportSchema = false
)
internal abstract class RecipeDatabase : RoomDatabase() {
    
    internal abstract fun getRecipeDao(): RecipeDao
    internal abstract fun getRecipeListDao(): RecipeListDao
    internal abstract fun getRecipeTypeDao(): RecipeTypeDao
    internal abstract fun getImageValidationDao(): ImageValidationDao
    
    companion object {
        
        const val RECIPE_DATABASE_NAME = "RecipeDatabase.db"
    }
}
