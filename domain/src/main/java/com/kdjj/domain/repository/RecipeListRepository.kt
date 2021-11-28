package com.kdjj.domain.repository

import com.kdjj.domain.model.Recipe

interface RecipeListRepository {
    
    suspend fun fetchOthersLatestRecipeListAfter(
        refresh: Boolean
    ): Result<List<Recipe>>

    suspend fun fetchOthersPopularRecipeListAfter(
        refresh: Boolean
    ): Result<List<Recipe>>

    suspend fun fetchOthersSearchRecipeListAfter(
        keyword: String,
        refresh: Boolean
    ): Result<List<Recipe>>

    suspend fun fetchMyLatestRecipeListAfter(
        index: Int
    ): Result<List<Recipe>>

    suspend fun fetchMyFavoriteRecipeListAfter(
        index: Int
    ): Result<List<Recipe>>

    suspend fun fetchMySearchRecipeListAfter(
        keyword: String,
        index: Int
    ): Result<List<Recipe>>

    suspend fun fetchMyTitleRecipeListAfter(
        index: Int
    ): Result<List<Recipe>>
}
