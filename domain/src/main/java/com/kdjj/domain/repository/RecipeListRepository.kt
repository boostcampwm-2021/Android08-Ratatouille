package com.kdjj.domain.repository

import com.kdjj.domain.model.Recipe

interface RecipeListRepository {

    suspend fun fetchRemoteLatestRecipeListAfter(lastVisibleCreateTime: Long): Result<List<Recipe>>
}