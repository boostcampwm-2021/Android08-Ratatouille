package com.kdjj.remote.dao

import com.kdjj.domain.model.Recipe

internal interface RemoteRecipeListDao {
    
    suspend fun fetchLatestRecipeListAfter(
        lastVisibleCreateTime: Long
    ): List<Recipe>
    
    suspend fun fetchPopularRecipeListAfter(
        lastVisibleViewCount: Int
    ): List<Recipe>
    
    suspend fun fetchSearchRecipeListAfter(
        keyword: String,
        lastVisibleTitle: String
    ): List<Recipe>
}
