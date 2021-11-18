package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class UpdateLocalRecipeFavoriteRequest(
    val recipe: Recipe
) : Request
