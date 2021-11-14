package com.kdjj.remote.dao

import com.kdjj.domain.model.RecipeType

internal interface FirestoreDao {
    
    suspend fun fetchRecipeTypes(): List<RecipeType>
}
