package com.kdjj.remote.dao

import com.kdjj.domain.model.Recipe

interface RecipeListDao {

    suspend fun fetchLatestRecipeListAfter(lastVisibleCreateTime: Long): List<Recipe>
}