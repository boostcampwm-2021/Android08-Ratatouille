package com.kdjj.remote.dao

import com.kdjj.domain.model.Recipe

internal interface RemoteRecipeListService {
    
    suspend fun fetchLatestRecipeListAfter(
        lastVisibleCreateTime: Long
    ): List<Recipe>
    
    suspend fun fetchPopularRecipeListAfter(
        refresh: Boolean
    ): List<Recipe>
    
    suspend fun fetchSearchRecipeListAfter(
        keyword: String,
        lastVisibleTitle: String
    ): List<Recipe>
}
