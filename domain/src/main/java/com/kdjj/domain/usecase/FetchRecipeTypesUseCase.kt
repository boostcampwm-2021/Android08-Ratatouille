package com.kdjj.domain.usecase

import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

class FetchRecipeTypesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {

    suspend operator fun invoke() = recipeRepository.fetchRecipeTypes()
}