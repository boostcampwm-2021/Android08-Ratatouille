package com.kdjj.ratatouille.di.data

import com.kdjj.data.recipe.RecipeLocalDataSource
import com.kdjj.data.recipe.RecipeRepositoryImpl
import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.local.dataSource.RecipeLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RecipeModule {
	
	@Binds
	@Singleton
	abstract fun bindRecipeLocalDataSource(recipeLocalDataSourceImpl: RecipeLocalDataSourceImpl): RecipeLocalDataSource
	
	@Binds
	@Singleton
	abstract fun bindRecipeRepository(recipeRepositoryImpl: RecipeRepositoryImpl): RecipeRepository
}
