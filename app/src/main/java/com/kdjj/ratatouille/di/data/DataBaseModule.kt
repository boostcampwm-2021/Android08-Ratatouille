package com.kdjj.ratatouille.di.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kdjj.local.database.RecipeDatabase
import com.kdjj.local.model.RecipeTypeEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
	
	@Provides
	@Singleton
	fun provideRecipeDataBase(@ApplicationContext context: Context): RecipeDatabase {
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
	fun provideRecipeDao(recipeDatabase: RecipeDatabase) = recipeDatabase.getRecipeDao()
	
	@Provides
	@Singleton
	fun provideRecipeTypeDao(recipeDatabase: RecipeDatabase) = recipeDatabase.getRecipeTypeDao()
}
