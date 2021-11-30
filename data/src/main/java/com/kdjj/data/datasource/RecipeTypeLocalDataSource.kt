package com.kdjj.data.datasource

import com.kdjj.domain.model.RecipeType

interface RecipeTypeLocalDataSource {
    
    suspend fun saveRecipeTypeList(
        recipeTypeList: List<RecipeType>
    ): Result<Unit>
    
    suspend fun fetchRecipeTypeList(): Result<List<RecipeType>>
}
