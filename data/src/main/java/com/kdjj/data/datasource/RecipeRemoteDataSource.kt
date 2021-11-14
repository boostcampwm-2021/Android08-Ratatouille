package com.kdjj.data.datasource

import com.kdjj.domain.model.Recipe

interface RecipeRemoteDataSource {
    
    suspend fun uploadRecipe(recipe: Recipe): Result<Unit>
}
