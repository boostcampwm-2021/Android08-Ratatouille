package com.kdjj.remote.di

import com.kdjj.data.datasource.RecipeImageRemoteDataSource
import com.kdjj.remote.dao.FirebaseStorageDao
import com.kdjj.remote.dao.FirebaseStorageDaoImpl
import com.kdjj.remote.datasource.RecipeImageRemoteDataSourceImpl
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
    internal abstract fun bindFireStorageDao(
        firebaseStorageDaoImpl: FirebaseStorageDaoImpl
    ): FirebaseStorageDao
}
