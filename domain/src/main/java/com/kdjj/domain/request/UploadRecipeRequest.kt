package com.kdjj.domain.request

import com.kdjj.domain.model.Recipe

data class UploadRecipeRequest(
    val recipe: Recipe
) : Request
