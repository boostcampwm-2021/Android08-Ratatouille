package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class UpdateRemoteRecipeRequest(
    val recipe: Recipe
) : Request
