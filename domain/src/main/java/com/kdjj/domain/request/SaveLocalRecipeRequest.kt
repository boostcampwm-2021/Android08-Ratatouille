package com.kdjj.domain.request

import com.kdjj.domain.model.Recipe

data class SaveLocalRecipeRequest(
    val recipe: Recipe
) : Request
