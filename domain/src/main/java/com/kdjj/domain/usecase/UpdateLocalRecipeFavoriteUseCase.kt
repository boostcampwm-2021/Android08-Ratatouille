package com.kdjj.domain.usecase

import com.kdjj.domain.model.request.UpdateLocalRecipeFavoriteRequest
import com.kdjj.domain.repository.RecipeRepository
import javax.inject.Inject

internal class UpdateLocalRecipeFavoriteUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ResultUseCase<UpdateLocalRecipeFavoriteRequest, Boolean> {

    override suspend fun invoke(request: UpdateLocalRecipeFavoriteRequest): Result<Boolean> =
        runCatching {
            val recipe = request.recipe
            val newFavoriteState = !recipe.isFavorite
            recipeRepository.updateLocalRecipe(recipe.copy(isFavorite = newFavoriteState)).getOrThrow()
            newFavoriteState
        }
}
