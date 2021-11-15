package com.kdjj.domain.usecase

import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.model.request.UpdateLocalRecipeFavoriteRequest
import javax.inject.Inject

internal class UpdateLocalRecipeFavoriteUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : UseCase<UpdateLocalRecipeFavoriteRequest, Boolean> {
    
    override suspend fun invoke(request: UpdateLocalRecipeFavoriteRequest): Result<Boolean> {
        val recipe = request.recipe
        return recipeRepository.updateLocalRecipe(recipe.copy(isFavorite = !recipe.isFavorite))
    }
}
