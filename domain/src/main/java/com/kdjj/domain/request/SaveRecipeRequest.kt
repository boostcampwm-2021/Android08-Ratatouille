package com.kdjj.domain.request

import com.kdjj.domain.model.Recipe

data class SaveRecipeRequest(
    val recipe: Recipe
) : Request
