package com.kdjj.remote.service

import com.kdjj.domain.model.Recipe
import com.kdjj.remote.dto.RecipeDto

internal interface RemoteRecipeListService {
    
    suspend fun fetchLatestRecipeListAfter(
        refresh: Boolean
    ): List<RecipeDto>
    
    suspend fun fetchPopularRecipeListAfter(
        refresh: Boolean
    ): List<RecipeDto>
    
    suspend fun fetchSearchRecipeListAfter(
        keyword: String,
        refresh: Boolean
    ): List<RecipeDto>
}
