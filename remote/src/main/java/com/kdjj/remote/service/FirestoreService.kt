package com.kdjj.remote.service

import com.kdjj.domain.model.RecipeType

internal interface FirestoreService {
    
    suspend fun fetchRecipeTypes(): List<RecipeType>
}
