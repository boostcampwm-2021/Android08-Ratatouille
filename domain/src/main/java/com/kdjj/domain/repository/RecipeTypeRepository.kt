package com.kdjj.domain.repository

import com.kdjj.domain.model.RecipeType

interface RecipeTypeRepository {
    
    suspend fun fetchRemoteRecipeTypeList(): Result<List<RecipeType>>
    
    suspend fun fetchLocalRecipeTypeList(): Result<List<RecipeType>>
    
    suspend fun saveRecipeTypeList(
        recipeTypeList: List<RecipeType>
    ): Result<Boolean>
}
