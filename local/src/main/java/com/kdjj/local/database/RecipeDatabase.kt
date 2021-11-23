package com.kdjj.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kdjj.local.dao.*
import com.kdjj.local.dto.*

@Database(
    entities = [
        RecipeMetaDto::class,
        RecipeTypeDto::class,
        RecipeStepDto::class,
        ImageValidationDto::class,
        RecipeTempMetaDto::class,
        RecipeTempStepDto::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class RecipeDatabase : RoomDatabase() {

    internal abstract fun getRecipeDao(): RecipeDao
    internal abstract fun getRecipeTempDao(): RecipeTempDao
    internal abstract fun getRecipeListDao(): RecipeListDao
    internal abstract fun getRecipeTypeDao(): RecipeTypeDao
    internal abstract fun getImageValidationDao(): ImageValidationDao

    companion object {

        const val RECIPE_DATABASE_NAME = "RecipeDatabase.db"
    }
}
