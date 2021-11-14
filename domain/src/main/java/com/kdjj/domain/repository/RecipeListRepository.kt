package com.kdjj.domain.repository

import com.kdjj.domain.model.Recipe

interface RecipeListRepository {
    
    suspend fun fetchRemoteLatestRecipeListAfter(
        lastVisibleCreateTime: Long
    ): Result<List<Recipe>>
    
    suspend fun fetchRemotePopularRecipeListAfter(
        lastVisibleViewCount: Int
    ): Result<List<Recipe>>
    
    suspend fun fetchRemoteSearchRecipeListAfter(
        keyword: String,
        lastVisibleTitle: String
    ): Result<List<Recipe>>
    
    suspend fun fetchLocalLatestRecipeListAfter(
        page: Int
    ): Result<List<Recipe>>
    
    suspend fun fetchLocalFavoriteRecipeListAfter(
        index: Int
    ): Result<List<Recipe>>
    
    suspend fun fetchLocalSearchRecipeListAfter(
        keyword: String,
        index: Int
    ): Result<List<Recipe>>
}
