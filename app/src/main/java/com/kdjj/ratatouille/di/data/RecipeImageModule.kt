package com.kdjj.ratatouille.di.data

import android.content.ContentResolver
import android.content.Context
import com.kdjj.data.recipeimage.RecipeImageLocalDataSource
import com.kdjj.data.recipeimage.RecipeImageRepositoryImpl
import com.kdjj.domain.repository.RecipeImageRepository
import com.kdjj.local.dataSource.RecipeImageLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RecipeImageModule {

    @Binds
    @Singleton
    abstract fun bindRecipeImageLocalDataSource(recipeImageLocalDataSourceImpl: RecipeImageLocalDataSourceImpl): RecipeImageLocalDataSource

    @Binds
    @Singleton
    abstract fun bindRecipeImageLocalRepository(recipeImageRepositoryImpl: RecipeImageRepositoryImpl): RecipeImageRepository

    companion object {

        @Provides
        @Singleton
        fun provideFileDir(@ApplicationContext context: Context): File = context.filesDir

        @Provides
        @Singleton
        fun provideContentResolver(@ApplicationContext context: Context): ContentResolver =
            context.contentResolver
    }

}