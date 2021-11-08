package com.kdjj.ratatouille.di.presentation

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.local.DAO.RecipeDAO
import com.kdjj.local.dataSource.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
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

            override suspend fun fetchRecipeTypes(): Result<List<RecipeType>> {
                return Result.success(listOf(
                    RecipeType(1, "한식"),
                    RecipeType(2, "중식"),
                    RecipeType(3, "양식"),
                    RecipeType(4, "일식"),
                ))
            }
        }
    }
}