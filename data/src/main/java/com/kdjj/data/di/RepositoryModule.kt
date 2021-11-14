package com.kdjj.data.di

import com.kdjj.data.repository.RecipeImageRepositoryImpl
import com.kdjj.data.repository.RecipeListRepositoryImpl
import com.kdjj.data.repository.RecipeRepositoryImpl
import com.kdjj.data.repository.RecipeTypeRepositoryImpl
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.domain.repository.RecipeListRepository
import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.repository.RecipeTypeRepository
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
}
