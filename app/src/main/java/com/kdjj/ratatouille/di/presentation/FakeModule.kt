package com.kdjj.ratatouille.di.presentation

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeRepository
import dagger.Module
import dagger.Provides

@Module
class FakeModule {
    /*
    아직 RecipeRepository Impl 이 구현 되지 않아 추가한 코드, 삭제해야 합니다.
     */
    @Provides
    fun providesRecipeRepository(): RecipeRepository {
        return object : RecipeRepository {
            override suspend fun saveRecipe(recipe: Recipe): Result<Boolean> {
                TODO("Not yet implemented")
            }

            override suspend fun fetchRecipeTypes(): Result<List<Recipe.Type>> {
                TODO("Not yet implemented")
            }
        }
    }
}