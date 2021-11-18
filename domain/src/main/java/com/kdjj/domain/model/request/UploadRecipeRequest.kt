package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class UploadRecipeRequest(
    val recipe: Recipe
) : Request
