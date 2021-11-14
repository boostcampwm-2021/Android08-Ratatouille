package com.kdjj.local.di

import com.kdjj.data.datasource.RecipeTypeLocalDataSource
import com.kdjj.local.dataSource.RecipeTypeLocalDataSourceImpl
import com.kdjj.local.database.RecipeDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalRecipeTypeModule {
    
    @Binds
    @Singleton
    internal abstract fun bindRecipeTypeLocalDataSource(
        recipeTypeLocalDataSourceImpl: RecipeTypeLocalDataSourceImpl
    ): RecipeTypeLocalDataSource
    
    companion object {
        
        @Provides
        @Singleton
        internal fun provideRecipeTypeDao(
            recipeDatabase: RecipeDatabase
        ) =
            recipeDatabase.getRecipeTypeDao()
    }
}
