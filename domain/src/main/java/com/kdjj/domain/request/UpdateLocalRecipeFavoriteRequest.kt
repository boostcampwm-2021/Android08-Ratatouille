package com.kdjj.domain.request

import com.kdjj.domain.model.Recipe

data class UpdateLocalRecipeFavoriteRequest(
    val recipe: Recipe
) : Request
