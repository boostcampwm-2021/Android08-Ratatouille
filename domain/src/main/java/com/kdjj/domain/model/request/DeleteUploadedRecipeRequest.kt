package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class DeleteUploadedRecipeRequest(
    val recipe: Recipe
) : Request
