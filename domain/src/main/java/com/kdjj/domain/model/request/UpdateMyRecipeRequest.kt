package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class UpdateMyRecipeRequest(
    val updatedRecipe: Recipe,
) : Request
