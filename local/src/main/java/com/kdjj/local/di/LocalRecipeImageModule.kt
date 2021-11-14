package com.kdjj.local.di

import com.kdjj.data.datasource.RecipeImageLocalDataSource
import com.kdjj.local.dataSource.RecipeImageLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalRecipeImageModule {
	
	@Binds
	@Singleton
	internal abstract fun bindRecipeImageLocalDataSource(
		recipeImageLocalDataSourceImpl: RecipeImageLocalDataSourceImpl
	): RecipeImageLocalDataSource
}
