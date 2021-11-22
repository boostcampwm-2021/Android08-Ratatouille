package com.kdjj.domain.di

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.model.request.*
import com.kdjj.domain.usecase.*
import dagger.Binds
import dagger.Module
import kotlinx.coroutines.flow.Flow

@Module
abstract class UseCaseModule {
    
    @Binds
    internal abstract fun bindDeleteLocalRecipeUseCase(
        deleteLocalRecipeUseCase: DeleteLocalRecipeUseCase
    ): ResultUseCase<DeleteLocalRecipeRequest, Boolean>
    
    @Binds
    internal abstract fun bindFetchLocalFavoriteRecipeListUseCase(
        fetchLocalFavoriteRecipeListUseCase: FetchLocalFavoriteRecipeListUseCase
    ): ResultUseCase<FetchLocalFavoriteRecipeListRequest, List<Recipe>>
    
    @Binds
    internal abstract fun bindFetchLocalLatestRecipeListUseCase(
        fetchLocalLatestRecipeListUseCase: FetchLocalLatestRecipeListUseCase
    ): ResultUseCase<FetchLocalLatestRecipeListRequest, List<Recipe>>
    
    @Binds
    internal abstract fun bindFechLocalTitleRecipeListUseCase(
        fetchLocalTitleRecipeListUseCase: FetchLocalTitleRecipeListUseCase
    ): ResultUseCase<FetchLocalTitleRecipeListRequest, List<Recipe>>
    
    @Binds
    internal abstract fun bindFetchLocalSearchRecipeListUseCase(
        fetchLocalSearchRecipeListUseCase: FetchLocalSearchRecipeListUseCase
    ): ResultUseCase<FetchLocalSearchRecipeListRequest, List<Recipe>>
    
    @Binds
    internal abstract fun bindFetchRecipeTypesUseCase(
        fetchRecipeTypeListUseCase: FetchRecipeTypeListUseCase
    ): ResultUseCase<EmptyRequest, List<RecipeType>>
    
    @Binds
    internal abstract fun bindFetchRemoteLatestRecipeListUseCase(
        fetchRemoteLatestRecipeListUseCase: FetchRemoteLatestRecipeListUseCase
    ): ResultUseCase<FetchRemoteLatestRecipeListRequest, List<Recipe>>
    
    @Binds
    internal abstract fun bindFetchRemotePopularRecipeListUseCase(
        fetchRemotePopularRecipeListUseCase: FetchRemotePopularRecipeListUseCase
    ): ResultUseCase<FetchRemotePopularRecipeListRequest, List<Recipe>>
    
    @Binds
    internal abstract fun bindFetchRemoteSearchRecipeListUseCase(
        fetchRemoteSearchRecipeListUseCase: FetchRemoteSearchRecipeListUseCase
    ): ResultUseCase<FetchRemoteSearchRecipeListRequest, List<Recipe>>
    
    @Binds
    internal abstract fun bindSaveLocalRecipeUseCase(
        saveLocalRecipeUseCase: SaveLocalRecipeUseCase
    ): ResultUseCase<SaveLocalRecipeRequest, Boolean>
    
    @Binds
    internal abstract fun bindUpdateLocalRecipeFavoriteUseCase(
        updateLocalRecipeFavoriteUseCase: UpdateLocalRecipeFavoriteUseCase
    ): ResultUseCase<UpdateLocalRecipeFavoriteRequest, Boolean>
    
    @Binds
    internal abstract fun bindUploadRecipeUseCase(
        uploadRecipeUseCase: UploadRecipeUseCase
    ): ResultUseCase<UploadRecipeRequest, Unit>
    
    @Binds
    internal abstract fun bindUpdateRemoteRecipeUseCase(
        updateRemoteRecipeUseCase: UpdateRemoteRecipeUseCase
    ): ResultUseCase<UpdateRemoteRecipeRequest, Unit>
    
    @Binds
    internal abstract fun bindIncreaseRemoteRecipeViewCountUseCase(
        increaseRemoteRecipeViewCountUseCase: IncreaseRemoteRecipeViewCountUseCase
    ): ResultUseCase<IncreaseRemoteRecipeViewCountRequest, Unit>
    
    @Binds
    internal abstract fun bindDeleteRemoteRecipeUseCase(
        deleteRemoteRecipeUseCase: DeleteRemoteRecipeUseCase
    ): ResultUseCase<DeleteRemoteRecipeRequest, Unit>
    
    @Binds
    internal abstract fun bindGetLocalRecipeFlowUseCase(
        getLocalRecipeFlowUseCase: GetLocalRecipeFlowUseCase
    ): ResultUseCase<GetLocalRecipeFlowRequest, Flow<Recipe>>

    @Binds
    internal abstract fun bindGetRecipeUpdateStateUseCase(
        getRecipeUpdateStateUseCase: GetRecipeUpdateStateUseCase
    ): ResultUseCase<EmptyRequest, Flow<Int>>

    @Binds
    internal abstract fun bindFetchRemoteRecipeUseCase(
        fetchRemoteRecipeUseCase: FetchRemoteRecipeUseCase
    ): ResultUseCase<FetchRemoteRecipeRequest, Recipe>
}
