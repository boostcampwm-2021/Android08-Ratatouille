package com.kdjj.presentation.model

import com.kdjj.domain.model.Recipe
import com.kdjj.presentation.common.calculateTotalTime
import com.kdjj.presentation.common.calculateUpdateTime

data class OthersRecipeModel(
    val recipeId: String,
    val title: String,
    val totalTime: String,
    val stuff: String,
    val updateTime: String,
    val viewCount: Int,
    val imgPath: String,
    val createTime: Long,
)

internal fun Recipe.toOthersRecipeModel() =
    OthersRecipeModel(
        this.recipeId,
        this.title,
        calculateTotalTime(this),
        this.stuff,
        calculateUpdateTime(this),
        this.viewCount,
        this.imgPath,
        this.createTime
    )
