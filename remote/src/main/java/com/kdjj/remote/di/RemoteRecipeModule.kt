package com.kdjj.remote.di

import com.kdjj.data.datasource.RecipeRemoteDataSource
import com.kdjj.remote.dao.RecipeDaoImpl
import com.kdjj.remote.dao.RemoteRecipeDao
import com.kdjj.remote.datasource.RecipeRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteRecipeModule {
    
    @Binds
    @Singleton
    internal abstract fun bindRecipeRemoteDataSource(
        recipeRemoteDataSourceImpl: RecipeRemoteDataSourceImpl
    ): RecipeRemoteDataSource
    
    @Binds
    @Singleton
    internal abstract fun provideRecipeDao(
        recipeDaoImpl: RecipeDaoImpl
    ): RemoteRecipeDao
}
