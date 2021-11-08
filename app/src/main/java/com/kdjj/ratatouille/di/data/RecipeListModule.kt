package com.kdjj.ratatouille.di.data

import com.kdjj.data.datasource.RecipeListRemoteDataSource
import com.kdjj.data.repository.RecipeListRepositoryImpl
import com.kdjj.domain.repository.RecipeListRepository
import com.kdjj.remote.dao.RecipeListDao
import com.kdjj.remote.dao.RecipeListDaoImpl
import com.kdjj.remote.datasource.RecipeListRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RecipeListModule {

    @Binds
    @Singleton
    abstract fun bindRecipeListRepository(recipeListRepositoryImpl: RecipeListRepositoryImpl): RecipeListRepository

    @Binds
    @Singleton
    abstract fun bindRecipeListRemoteDataSource(recipeListRemoteDataSourceImpl: RecipeListRemoteDataSourceImpl): RecipeListRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindRecipeListDao(recipeListDaoImpl: RecipeListDaoImpl): RecipeListDao
}