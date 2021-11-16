package com.kdjj.presentation.model

import com.kdjj.domain.model.Recipe

data class OthersRecipeModel(
    val recipeId: String,
    val title: String,
    val totalTime: Int,
    val stuff: String,
    val viewCount: Int,
    val imgPath: String,
    val createTime: Long,
)

internal fun Recipe.toOthersRecipeModel() =
    OthersRecipeModel(
        recipeId,
        title,
        stepList.map { it.seconds }.reduce { acc, i -> acc + i },
        stuff,
        viewCount,
        imgPath,
        createTime
    )
