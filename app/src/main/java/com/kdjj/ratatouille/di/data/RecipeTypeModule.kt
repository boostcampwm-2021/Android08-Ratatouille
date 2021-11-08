package com.kdjj.ratatouille.di.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kdjj.data.recipetype.RecipeTypeLocalDataSource
import com.kdjj.data.recipetype.RecipeTypeRemoteDataSource
import com.kdjj.data.recipetype.RecipeTypeRepositoryImpl
import com.kdjj.domain.repository.RecipeTypeRepository
import com.kdjj.local.dataSource.RecipeTypeLocalDataSourceImpl
import com.kdjj.remote.FirestoreDao
import com.kdjj.remote.FirestoreDaoImpl
import com.kdjj.remote.recipe.RecipeTypeRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeTypeModule {
	
	@Provides
	@Singleton
	fun provideFireStore() = Firebase.firestore
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RecipeTypeDataSourceModule {
	
	@Binds
	@Singleton
	abstract fun bindRecipeTypeRemoteDataSource(recipeTypeRemoteDataSourceImpl: RecipeTypeRemoteDataSourceImpl): RecipeTypeRemoteDataSource
	
	@Binds
	@Singleton
	abstract fun bindFireStoreDao(firestoreDaoImpl: FirestoreDaoImpl): FirestoreDao
	
	@Binds
	@Singleton
	abstract fun bindRecipeTypeLocalDataSource(recipeTypeLocalDataSourceImpl: RecipeTypeLocalDataSourceImpl): RecipeTypeLocalDataSource
	
	@Binds
	@Singleton
	abstract fun bindRecipeTypeRepository(recipeTypeRepositoryImpl: RecipeTypeRepositoryImpl): RecipeTypeRepository
}