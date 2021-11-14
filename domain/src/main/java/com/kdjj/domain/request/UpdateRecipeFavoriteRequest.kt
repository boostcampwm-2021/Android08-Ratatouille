package com.kdjj.domain.request

import com.kdjj.domain.model.Recipe

data class UpdateRecipeFavoriteRequest(
    val recipe: Recipe
) : Request
