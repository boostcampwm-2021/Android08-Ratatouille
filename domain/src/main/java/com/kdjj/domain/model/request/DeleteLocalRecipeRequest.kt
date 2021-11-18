package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class DeleteLocalRecipeRequest(
    val recipe: Recipe
) : Request
