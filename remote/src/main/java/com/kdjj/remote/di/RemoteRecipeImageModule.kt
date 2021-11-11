package com.kdjj.remote.di

import com.kdjj.data.recipeimage.RecipeImageRemoteDataSource
import com.kdjj.remote.FirebaseStorageDao
import com.kdjj.remote.FirebaseStorageDaoImpl
import com.kdjj.remote.recipe.RecipeImageRemoteDataSourceImpl
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
	internal abstract fun bindRecipeImageRemoteDataSource(recipeImageRemoteDataSourceImpl: RecipeImageRemoteDataSourceImpl): RecipeImageRemoteDataSource
	
	@Binds
	@Singleton
	internal abstract fun bindFireStorageDao(firebaseStorageDaoImpl: FirebaseStorageDaoImpl): FirebaseStorageDao
}
