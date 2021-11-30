package com.kdjj.remote.di

import com.kdjj.data.datasource.RecipeImageRemoteDataSource
import com.kdjj.remote.datasource.RecipeImageRemoteDataSourceImpl
import com.kdjj.remote.service.RecipeImageService
import com.kdjj.remote.service.RecipeImageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteRecipeImageModule {
    
    @Binds
    @Singleton
    internal abstract fun bindRecipeImageRemoteDataSource(
        recipeImageRemoteDataSourceImpl: RecipeImageRemoteDataSourceImpl
    ): RecipeImageRemoteDataSource

    @Binds
    @Singleton
    internal abstract fun bindRecipeImageService(
        recipeImageServiceImpl: RecipeImageServiceImpl
    ): RecipeImageService
}
