package com.kdjj.remote

import com.kdjj.domain.model.RecipeType

internal interface FirestoreDao {

	suspend fun fetchRecipeTypes(): List<RecipeType>
}
