package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class DeleteMyRecipeRequest(
    val recipe: Recipe
) : Request
