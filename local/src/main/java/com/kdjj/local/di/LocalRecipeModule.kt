package com.kdjj.local.di

import com.kdjj.data.datasource.RecipeLocalDataSource
import com.kdjj.local.dataSource.RecipeLocalDataSourceImpl
import com.kdjj.local.database.RecipeDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalRecipeModule {
    
    @Binds
    @Singleton
    internal abstract fun bindRecipeLocalDataSource(
        recipeLocalDataSourceImpl: RecipeLocalDataSourceImpl
    ): RecipeLocalDataSource
    
    companion object {
        
        @Provides
        @Singleton
        internal fun provideRecipeDao(
            recipeDatabase: RecipeDatabase
        ) =
            recipeDatabase.getRecipeDao()
    }
}
