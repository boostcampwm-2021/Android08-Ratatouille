package com.kdjj.local.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kdjj.local.database.RecipeDatabase
import com.kdjj.local.dto.RecipeTypeEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    
    @Provides
    @Singleton
    internal fun provideRecipeDataBase(
        @ApplicationContext context: Context
    ): RecipeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RecipeDatabase::class.java,
            RecipeDatabase.RECIPE_DATABASE_NAME
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    RecipeTypeEntity.defaultTypes.forEach { recipeType ->
                        db.execSQL("INSERT INTO RecipeType VALUES (${recipeType.recipeTypeId}, '${recipeType.title}');")
                    }
                }
            }
        }).build()
    }
    
    @Provides
    @Singleton
    fun provideFileDir(
        @ApplicationContext context: Context
    ): File =
        context.filesDir
    
    @Provides
    @Singleton
    fun provideContentResolver(
        @ApplicationContext context: Context
    ): ContentResolver =
        context.contentResolver
}
