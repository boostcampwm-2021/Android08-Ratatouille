package com.kdjj.local.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kdjj.domain.model.RecipeType
import com.kdjj.local.DAO.RecipeDAO
import com.kdjj.local.model.RecipeMetaEntity
import com.kdjj.local.model.RecipeStepEntity
import com.kdjj.local.model.RecipeTypeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    ).addCallback(object: RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                RecipeTypeEntity.defaultTypes.forEach{ recipeType ->
                                    getInstance(context).getRecipeDao().insertRecipeType(recipeType)
                                }
                            }
                        }
                    }).build()
                }
            }
            return INSTANCE!!
        }
    }
}