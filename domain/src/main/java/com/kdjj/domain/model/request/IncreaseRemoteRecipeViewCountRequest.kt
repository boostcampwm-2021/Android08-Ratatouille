package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class IncreaseRemoteRecipeViewCountRequest(
    val recipe: Recipe
) : Request
