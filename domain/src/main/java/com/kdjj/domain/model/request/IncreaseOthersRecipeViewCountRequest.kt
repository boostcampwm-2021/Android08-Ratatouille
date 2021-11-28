package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class IncreaseOthersRecipeViewCountRequest(
    val recipe: Recipe
) : Request
