package com.kdjj.remote.di

import com.kdjj.data.datasource.RecipeTypeRemoteDataSource
import com.kdjj.remote.datasource.RecipeTypeRemoteDataSourceImpl
import com.kdjj.remote.service.RecipeTypeService
import com.kdjj.remote.service.RecipeTypeServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteRecipeTypeModule {
    
    @Binds
    @Singleton
    internal abstract fun bindRecipeTypeRemoteDataSource(
        recipeTypeRemoteDataSourceImpl: RecipeTypeRemoteDataSourceImpl
    ): RecipeTypeRemoteDataSource

    @Binds
    @Singleton
    internal abstract fun bindRecipeTypeService(
        recipeTypeServiceImpl: RecipeTypeServiceImpl
    ): RecipeTypeService
}
