package com.kdjj.remote.service

import com.kdjj.remote.dto.RecipeTypeDto

internal interface RecipeTypeService {

    suspend fun fetchRecipeTypes(): List<RecipeTypeDto>
}
