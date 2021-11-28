package com.kdjj.domain.di

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.model.request.*
import com.kdjj.domain.usecase.*
import dagger.Binds
import dagger.Module

@Module
abstract class ResultUseCaseModule {

    @Binds
    internal abstract fun bindDeleteMyRecipeUseCase(
        deleteMyRecipeUseCase: DeleteMyRecipeUseCase
    ): ResultUseCase<DeleteMyRecipeRequest, Boolean>

    @Binds
    internal abstract fun bindFetchMyFavoriteRecipeListUseCase(
        fetchMyFavoriteRecipeListUseCase: FetchMyFavoriteRecipeListUseCase
    ): ResultUseCase<FetchMyFavoriteRecipeListRequest, List<Recipe>>

    @Binds
    internal abstract fun bindFetchMyLatestRecipeListUseCase(
        fetchMyLatestRecipeListUseCase: FetchMyLatestRecipeListUseCase
    ): ResultUseCase<FetchMyLatestRecipeListRequest, List<Recipe>>

    @Binds
    internal abstract fun bindFetchMyTitleRecipeListUseCase(
        fetchMyTitleRecipeListUseCase: FetchMyTitleRecipeListUseCase
    ): ResultUseCase<FetchMyTitleRecipeListRequest, List<Recipe>>

    @Binds
    internal abstract fun bindFetchMySearchRecipeListUseCase(
        fetchMySearchRecipeListUseCase: FetchMySearchRecipeListUseCase
    ): ResultUseCase<FetchMySearchRecipeListRequest, List<Recipe>>

    @Binds
    internal abstract fun bindFetchRecipeTypesUseCase(
        fetchRecipeTypeListUseCase: FetchRecipeTypeListUseCase
    ): ResultUseCase<EmptyRequest, List<RecipeType>>

    @Binds
    internal abstract fun bindFetchOthersLatestRecipeListUseCase(
        fetchOthersLatestRecipeListUseCase: FetchOthersLatestRecipeListUseCase
    ): ResultUseCase<FetchOthersLatestRecipeListRequest, List<Recipe>>

    @Binds
    internal abstract fun bindFetchOthersPopularRecipeListUseCase(
        fetchOthersPopularRecipeListUseCase: FetchOthersPopularRecipeListUseCase
    ): ResultUseCase<FetchOthersPopularRecipeListRequest, List<Recipe>>

    @Binds
    internal abstract fun bindFetchOthersSearchRecipeListUseCase(
        fetchOthersSearchRecipeListUseCase: FetchOthersSearchRecipeListUseCase
    ): ResultUseCase<FetchOthersSearchRecipeListRequest, List<Recipe>>

    @Binds
    internal abstract fun bindSaveMyRecipeUseCase(
        saveMyRecipeUseCase: SaveMyRecipeUseCase
    ): ResultUseCase<SaveMyRecipeRequest, Boolean>

    @Binds
    internal abstract fun bindUpdateMyRecipeFavoriteUseCase(
        updateMyRecipeFavoriteUseCase: UpdateMyRecipeFavoriteUseCase
    ): ResultUseCase<UpdateMyRecipeFavoriteRequest, Boolean>

    @Binds
    internal abstract fun bindUploadRecipeUseCase(
        uploadRecipeUseCase: UploadRecipeUseCase
    ): ResultUseCase<UploadRecipeRequest, Unit>

    @Binds
    internal abstract fun bindUpdateUploadedRecipeUseCase(
        updateUploadedRecipeUseCase: UpdateUploadedRecipeUseCase
    ): ResultUseCase<UpdateUploadedRecipeRequest, Unit>

    @Binds
    internal abstract fun bindIncreaseOthersRecipeViewCountUseCase(
        increaseOthersRecipeViewCountUseCase: IncreaseOthersRecipeViewCountUseCase
    ): ResultUseCase<IncreaseOthersRecipeViewCountRequest, Unit>

    @Binds
    internal abstract fun bindDeleteUploadedRecipeUseCase(
        deleteUploadedRecipeUseCase: DeleteUploadedRecipeUseCase
    ): ResultUseCase<DeleteUploadedRecipeRequest, Unit>

    @Binds
    internal abstract fun bindFetchOthersRecipeUseCase(
        fetchOthersRecipeUseCase: FetchOthersRecipeUseCase
    ): ResultUseCase<FetchOthersRecipeRequest, Recipe>

    @Binds
    internal abstract fun bindUpdateMyRecipeUseCase(
        updateMyRecipeUseCase: UpdateMyRecipeUseCase
    ): ResultUseCase<UpdateMyRecipeRequest, Unit>

    @Binds
    internal abstract fun bindFetchRecipeTempUseCase(
        fetchRecipeTempUseCase: FetchRecipeTempUseCase
    ) : ResultUseCase<FetchRecipeTempRequest, Recipe?>

    @Binds
    internal abstract fun bindSaveRecipeTempUseCase(
        saveRecipeTempUseCase: SaveRecipeTempUseCase
    ) : ResultUseCase<SaveRecipeTempRequest, Unit>

    @Binds
    internal abstract fun bindGetMyRecipeUseCase(
        getMyRecipeUseCase: GetMyRecipeUseCase
    ): ResultUseCase<GetMyRecipeRequest, Recipe>

    @Binds
    internal abstract fun bindDeleteRecipeTempUseCase(
        deleteRecipeTempUseCase: DeleteRecipeTempUseCase
    ) : ResultUseCase<DeleteRecipeTempRequest, Unit>

    @Binds
    internal abstract fun bindDeleteUselessImageFileUseCase(
        deleteUselessImageFileUseCase: DeleteUselessImageFileUseCase
    ) : ResultUseCase<EmptyRequest, Unit>
}
