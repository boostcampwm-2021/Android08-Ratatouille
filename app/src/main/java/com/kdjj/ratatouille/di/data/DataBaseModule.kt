package com.kdjj.ratatouille.di.data

import android.content.Context
import com.kdjj.local.database.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
	
	@Provides
	@Singleton
	fun provideRecipeDataBase(@ApplicationContext context: Context) =
		RecipeDatabase.getInstance(context)
	
	@Provides
	@Singleton
	fun provideRecipeDao(recipeDatabase: RecipeDatabase) = recipeDatabase.getRecipeDao()
}