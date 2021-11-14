package com.kdjj.domain.request

import com.kdjj.domain.model.Recipe

data class DeleteRecipeRequest(
    val recipe: Recipe
) : Request
