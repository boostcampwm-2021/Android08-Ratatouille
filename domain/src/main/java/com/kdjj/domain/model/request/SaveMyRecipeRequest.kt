package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class SaveMyRecipeRequest(
    val recipe: Recipe
) : Request
