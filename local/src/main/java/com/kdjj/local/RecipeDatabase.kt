package com.kdjj.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kdjj.local.DAO.RecipeDAO
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

    companion object {
        private var INSTANCE: RecipeDatabase? = null

        fun getInstance(context: Context): RecipeDatabase {
            if(INSTANCE == null){
                synchronized(RoomDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context, RecipeDatabase::class.java, "RecipeDatabase.db"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}