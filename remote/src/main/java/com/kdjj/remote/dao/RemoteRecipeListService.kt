package com.kdjj.remote.dao

import com.kdjj.domain.model.Recipe

internal interface RemoteRecipeListService {
    
    suspend fun fetchLatestRecipeListAfter(
        refresh: Boolean
    ): List<Recipe>
    
    suspend fun fetchPopularRecipeListAfter(
        refresh: Boolean
    ): List<Recipe>
    
    suspend fun fetchSearchRecipeListAfter(
        keyword: String,
        refresh: Boolean
    ): List<Recipe>
}
