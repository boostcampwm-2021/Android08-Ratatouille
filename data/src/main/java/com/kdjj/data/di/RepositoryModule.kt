package com.kdjj.data.di

import com.kdjj.data.repository.RecipeListRepositoryImpl
import com.kdjj.domain.repository.RecipeListRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    internal abstract fun bindRecipeListRepository(recipeListRepositoryImpl: RecipeListRepositoryImpl): RecipeListRepository
}