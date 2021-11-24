package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class DeleteRecipeTempRequest(
    val recipe: Recipe
) : Request
