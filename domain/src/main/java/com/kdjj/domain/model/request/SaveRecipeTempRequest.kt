package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class SaveRecipeTempRequest(
    val recipe: Recipe
) : Request