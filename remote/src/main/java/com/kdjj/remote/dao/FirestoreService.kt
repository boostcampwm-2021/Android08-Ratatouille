package com.kdjj.remote.dao

import com.kdjj.domain.model.RecipeType

internal interface FirestoreService {
    
    suspend fun fetchRecipeTypes(): List<RecipeType>
}
