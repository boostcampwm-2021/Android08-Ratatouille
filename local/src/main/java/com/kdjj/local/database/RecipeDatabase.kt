package com.kdjj.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kdjj.local.DAO.RecipeDAO
import com.kdjj.local.DAO.RecipeListDao
import com.kdjj.local.model.RecipeMetaEntity
import com.kdjj.local.model.RecipeStepEntity
import com.kdjj.local.model.RecipeTypeEntity

@Database(
	entities = [RecipeMetaEntity::class, RecipeTypeEntity::class, RecipeStepEntity::class],
	version = 1,
	exportSchema = false
)
abstract class RecipeDatabase : RoomDatabase() {
	
	abstract fun getRecipeDao(): RecipeDAO
	abstract fun getRecipeListDao(): RecipeListDao
	
	companion object {
		
		const val RECIPE_DATABASE_NAME = "RecipeDatabase.db"
	}
}
