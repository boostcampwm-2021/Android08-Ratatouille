package com.kdjj.domain.usecase

import com.kdjj.domain.repository.RecipeRepository
import com.kdjj.domain.request.UpdateRecipeFavoriteRequest
import javax.inject.Inject

internal class UpdateRecipeFavoriteUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : UseCase<UpdateRecipeFavoriteRequest, Boolean> {
    
    override suspend fun invoke(request: UpdateRecipeFavoriteRequest): Result<Boolean> {
        val recipe = request.recipe
        return recipeRepository.updateRecipe(recipe.copy(isFavorite = !recipe.isFavorite))
    }
}
