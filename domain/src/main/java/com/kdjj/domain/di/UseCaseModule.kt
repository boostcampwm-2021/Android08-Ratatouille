package com.kdjj.domain.di

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.request.*
import com.kdjj.domain.usecase.*
import com.kdjj.domain.usecase.FetchLocalLatestRecipeListUseCase
import com.kdjj.domain.usecase.FetchRemoteLatestRecipeListUseCase
import com.kdjj.domain.usecase.SaveRecipeUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class UseCaseModule {
    @Binds
    internal abstract fun bindSaveRecipeUseCase(saveRecipeUseCase: SaveRecipeUseCase): UseCase<RecipeRequest, Boolean>

    @Binds
    internal abstract fun bindFetchRecipeTypesUseCase(fetchRecipeTypesUseCase: FetchRecipeTypesUseCase): UseCase<EmptyRequest, List<RecipeType>>

    @Binds
    internal abstract fun bindFetchLocalLatestRecipeListUseCase(fetchLocalLatestRecipeListUseCase: FetchLocalLatestRecipeListUseCase): UseCase<FetchLocalLatestRecipeListRequest, List<Recipe>>

    @Binds
    internal abstract fun bindFetchRemoteLatestRecipeListUseCase(fetchRemoteLatestRecipeListUseCase: FetchRemoteLatestRecipeListUseCase): UseCase<FetchRemoteLatestRecipeListRequest, List<Recipe>>

    @Binds
    internal abstract fun bindFetchRemotePopularRecipeListUseCase(fetchRemotePopularRecipeListUseCase: FetchRemotePopularRecipeListUseCase): UseCase<FetchRemotePopularRecipeListRequest, List<Recipe>>

    @Binds
    internal abstract fun bindFetchRemoteSearchRecipeListUseCase(fetchRemoteSearchRecipeListUseCase: FetchRemoteSearchRecipeListUseCase): UseCase<FetchRemoteSearchRecipeListRequest, List<Recipe>>
}