package com.kdjj.presentation.model

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState

data class RecipeListItemModel(
    val recipeId: String,
    val title: String,
    val totalTime: Int,
    val stuff: String,
    val viewCount: Int,
    val imgPath: String,
    val createTime: Long,
    val state: RecipeState,
)

internal fun Recipe.toRecipeListItemModel() =
    RecipeListItemModel(
        recipeId,
        title,
        stepList.map { it.seconds }.fold(0) { acc, i -> acc + i },
        stuff,
        viewCount,
        imgPath,
        createTime,
        state
    )
