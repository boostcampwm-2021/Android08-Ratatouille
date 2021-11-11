package com.kdjj.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kdjj.local.dao.RecipeListDao
import com.kdjj.local.dao.RecipeDao
import com.kdjj.local.dao.RecipeTypeDao
import com.kdjj.local.model.RecipeMetaEntity
import com.kdjj.local.model.RecipeStepEntity
import com.kdjj.local.model.RecipeTypeEntity

@Database(
	entities = [RecipeMetaEntity::class, RecipeTypeEntity::class, RecipeStepEntity::class],
	version = 1,
	exportSchema = false
)
internal abstract class RecipeDatabase : RoomDatabase() {
	
	internal abstract fun getRecipeDao(): RecipeDao
	internal abstract fun getRecipeListDao(): RecipeListDao
	internal abstract fun getRecipeTypeDao(): RecipeTypeDao
	
	companion object {
		
		const val RECIPE_DATABASE_NAME = "RecipeDatabase.db"
	}
}
