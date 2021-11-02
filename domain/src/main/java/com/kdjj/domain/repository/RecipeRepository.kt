package com.kdjj.domain.repository

import com.kdjj.domain.model.Recipe

interface RecipeRepository {

    suspend fun saveRecipe(recipe: Recipe): Result<Boolean>

    suspend fun fetchRecipeTypes(): Result<List<Recipe.Type>>
}