package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class UpdateLocalRecipeRequest(
    val updatedRecipe: Recipe,
) : Request
