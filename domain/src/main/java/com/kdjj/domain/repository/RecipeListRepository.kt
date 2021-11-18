package com.kdjj.domain.repository

import com.kdjj.domain.model.Recipe

interface RecipeListRepository {
    
    suspend fun fetchRemoteLatestRecipeListAfter(
        refresh: Boolean
    ): Result<List<Recipe>>
    
    suspend fun fetchRemotePopularRecipeListAfter(
        refresh: Boolean
    ): Result<List<Recipe>>
    
    suspend fun fetchRemoteSearchRecipeListAfter(
        keyword: String,
        refresh: Boolean
    ): Result<List<Recipe>>
    
    suspend fun fetchLocalLatestRecipeListAfter(
        index: Int
    ): Result<List<Recipe>>
    
    suspend fun fetchLocalFavoriteRecipeListAfter(
        index: Int
    ): Result<List<Recipe>>
    
    suspend fun fetchLocalSearchRecipeListAfter(
        keyword: String,
        index: Int
    ): Result<List<Recipe>>

    suspend fun fetchLocalTitleRecipeListAfter(
        index: Int
    ): Result<List<Recipe>>
}
