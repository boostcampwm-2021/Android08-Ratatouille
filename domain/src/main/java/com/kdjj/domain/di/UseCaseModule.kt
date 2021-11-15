package com.kdjj.domain.di

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.request.*
import com.kdjj.domain.usecase.*
import dagger.Binds
import dagger.Module

@Module
abstract class UseCaseModule {
    
    @Binds
    internal abstract fun bindDeleteRecipeUseCase(
        deleteLocalRecipeUseCase: DeleteLocalRecipeUseCase
    ): UseCase<DeleteLocalRecipeRequest, Boolean>
    
    @Binds
    internal abstract fun bindFetchLocalFavoriteRecipeListUseCase(
        fetchLocalFavoriteRecipeListUseCase: FetchLocalFavoriteRecipeListUseCase
    ): UseCase<FetchLocalFavoriteRecipeListRequest, List<Recipe>>
    
    @Binds
    internal abstract fun bindFetchLocalLatestRecipeListUseCase(
        fetchLocalLatestRecipeListUseCase: FetchLocalLatestRecipeListUseCase
    ): UseCase<FetchLocalLatestRecipeListRequest, List<Recipe>>
    
    @Binds
    internal abstract fun bindFetchLocalSearchRecipeListUseCase(
        fetchLocalSearchRecipeListUseCase: FetchLocalSearchRecipeListUseCase
    ): UseCase<FetchLocalSearchRecipeListRequest, List<Recipe>>
    
    @Binds
    internal abstract fun bindFetchRecipeTypesUseCase(
        fetchRecipeTypesUseCase: FetchRecipeTypesUseCase
    ): UseCase<EmptyRequest, List<RecipeType>>
    
    @Binds
    internal abstract fun bindFetchRemoteLatestRecipeListUseCase(
        fetchRemoteLatestRecipeListUseCase: FetchRemoteLatestRecipeListUseCase
    ): UseCase<FetchRemoteLatestRecipeListRequest, List<Recipe>>
    
    @Binds
    internal abstract fun bindFetchRemotePopularRecipeListUseCase(
        fetchRemotePopularRecipeListUseCase: FetchRemotePopularRecipeListUseCase
    ): UseCase<FetchRemotePopularRecipeListRequest, List<Recipe>>
    
    @Binds
    internal abstract fun bindFetchRemoteSearchRecipeListUseCase(
        fetchRemoteSearchRecipeListUseCase: FetchRemoteSearchRecipeListUseCase
    ): UseCase<FetchRemoteSearchRecipeListRequest, List<Recipe>>
    
    @Binds
    internal abstract fun bindSaveRecipeUseCase(
        saveLocalRecipeUseCase: SaveLocalRecipeUseCase
    ): UseCase<SaveLocalRecipeRequest, Boolean>
    
    @Binds
    internal abstract fun bindUpdateRecipeFavoriteUseCase(
        updateLocalRecipeFavoriteUseCase: UpdateLocalRecipeFavoriteUseCase
    ): UseCase<UpdateLocalRecipeFavoriteRequest, Boolean>
    
    @Binds
    internal abstract fun bindUploadRecipeUseCase(
        uploadRecipeUseCase: UploadRecipeUseCase
    ): UseCase<UploadRecipeRequest, Unit>
    
    @Binds
    internal abstract fun bindUpdateRemoteRecipeUseCase(
        updateRemoteRecipeUseCase: UpdateRemoteRecipeUseCase
    ): UseCase<UpdateRemoteRecipeRequest, Unit>
}
