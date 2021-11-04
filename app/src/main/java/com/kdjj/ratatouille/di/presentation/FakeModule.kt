package com.kdjj.ratatouille.di.presentation

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.request.EmptyRequest
import com.kdjj.domain.usecase.FetchRecipeTypesUseCase
import com.kdjj.domain.usecase.UseCase
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
                TODO("Not yet implemented")
            }
        }
    }

    @Provides
    fun provideFetchRecipeTypesUseCase(repository: RecipeRepository): UseCase<EmptyRequest, List<RecipeType>> {
        return FetchRecipeTypesUseCase(repository)
    }
}