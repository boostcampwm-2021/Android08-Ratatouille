package com.kdjj.domain.usecase

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

class SaveRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {

    suspend operator fun invoke(recipe: Recipe) = recipeRepository.saveRecipe(recipe)
}