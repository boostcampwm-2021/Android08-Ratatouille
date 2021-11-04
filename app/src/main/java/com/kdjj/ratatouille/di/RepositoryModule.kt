package com.kdjj.ratatouille.di

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.repository.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

//    @Binds
//    abstract fun provideRecipeRepository(recipeRepositoryImpl: RecipeRepositoryImpl): RecipeRepository
}