package com.kdjj.remote.service

import com.kdjj.remote.dto.RecipeTypeDto

internal interface FirestoreService {

    suspend fun fetchRecipeTypes(): List<RecipeTypeDto>
}
