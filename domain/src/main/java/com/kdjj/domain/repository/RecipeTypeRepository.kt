package com.kdjj.domain.repository

import com.kdjj.domain.model.RecipeType

interface RecipeTypeRepository {
    
    suspend fun fetchRecipeTypeList(): Result<List<RecipeType>>
}
