package com.kdjj.data.di

import com.kdjj.data.repository.*
import com.kdjj.data.repository.RecipeImageRepositoryImpl
import com.kdjj.data.repository.RecipeListRepositoryImpl
import com.kdjj.data.repository.RecipeRepositoryImpl
import com.kdjj.data.repository.RecipeTypeRepositoryImpl
import com.kdjj.domain.repository.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    internal abstract fun bindRecipeListRepository(
        recipeListRepositoryImpl: RecipeListRepositoryImpl
    ): RecipeListRepository
    
    @Binds
    @Singleton
    internal abstract fun bindRecipeTypeRepository(
        recipeTypeRepositoryImpl: RecipeTypeRepositoryImpl
    ): RecipeTypeRepository
    
    @Binds
    @Singleton
    internal abstract fun bindRecipeRepository(
        recipeRepositoryImpl: RecipeRepositoryImpl
    ): RecipeRepository
    
    @Binds
    @Singleton
    internal abstract fun bindRecipeImageRepository(
        recipeImageRepositoryImpl: RecipeImageRepositoryImpl
    ): RecipeImageRepository

    @Binds
    @Singleton
    internal abstract fun bindRecipeTempRepository(
        recipeTempRepositoryImpl: RecipeTempRepositoryImpl
    ): RecipeTempRepository
}
