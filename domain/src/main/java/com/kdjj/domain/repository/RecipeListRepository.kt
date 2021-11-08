package com.kdjj.domain.repository

import com.kdjj.domain.model.Recipe

interface RecipeListRepository {

    suspend fun fetchRemoteLatestRecipeList(lastVisibleCreateTime: Long): Result<List<Recipe>>
}