package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.UpdateMyRecipeFavoriteRequest
import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

internal class UpdateMyRecipeFavoriteUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ResultUseCase<UpdateMyRecipeFavoriteRequest, Boolean> {

    override suspend fun invoke(request: UpdateMyRecipeFavoriteRequest): Result<Boolean> {
        val recipe = request.recipe
        val newFavoriteState = !recipe.isFavorite
        return recipeRepository.updateMyRecipe(recipe.copy(isFavorite = newFavoriteState)).map {
            newFavoriteState
        }
    }
}
