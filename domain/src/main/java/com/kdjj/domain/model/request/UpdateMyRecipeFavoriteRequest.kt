package com.kdjj.domain.model.request

import com.kdjj.domain.model.Recipe

data class UpdateMyRecipeFavoriteRequest(
    val recipe: Recipe
) : Request
