package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class DeleteRemoteRecipeRequest(
    val recipe: Recipe
) : Request
