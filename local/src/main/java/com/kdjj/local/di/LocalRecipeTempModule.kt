package com.kdjj.local.di

import com.kdjj.data.datasource.RecipeTempLocalDataSource
import com.kdjj.local.dataSource.RecipeTempLocalDataSourceImpl
import com.kdjj.local.database.RecipeDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalRecipeTempModule {
    
    @Binds
    @Singleton
    internal abstract fun bindRecipeTempLocalDataSource(
        recipeTempLocalDataSourceImpl: RecipeTempLocalDataSourceImpl
    ): RecipeTempLocalDataSource
    
    companion object {
        
        @Provides
        @Singleton
        internal fun provideRecipeTempDao(
            recipeDatabase: RecipeDatabase
        ) = recipeDatabase.getRecipeTempDao()
    }
}
