package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class FetchRemoteRecipeRequest(
    val recipeID: String
): Request