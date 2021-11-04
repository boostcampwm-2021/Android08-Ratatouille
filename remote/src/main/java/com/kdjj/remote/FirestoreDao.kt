package com.kdjj.remote

import com.kdjj.domain.model.RecipeType

interface FirestoreDao {

	suspend fun fetchRecipeTypes(): List<RecipeType>
}