package com.kdjj.local.di

import com.kdjj.data.datasource.RecipeListLocalDataSource
import com.kdjj.local.dataSource.RecipeListLocalDataSourceImpl
import com.kdjj.local.database.RecipeDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalRecipeListModule {
    
    @Binds
    @Singleton
    internal abstract fun bindRecipeListLocalDataSource(
        recipeListLocalDataSourceImpl: RecipeListLocalDataSourceImpl
    ): RecipeListLocalDataSource
    
    companion object {
        
        @Provides
        @Singleton
        internal fun provideRecipeListDao(
            recipeDatabase: RecipeDatabase
        ) =
            recipeDatabase.getRecipeListDao()
    }
}
