package com.kdjj.remote.entity

import com.kdjj.domain.model.RecipeState

data class RecipeEntity(
    val recipeId: String = "",
    val title: String = "",
    val type: RecipeTypeEntity = RecipeTypeEntity(0, ""),
    val stuff: String = "",
    val imgPath: String = "",
    val stepList: List<RecipeStepEntity> = listOf(),
    val authorId: String = "",
    val viewCount: Int = 0,
    val createTime: Long = 0L,
    val state: RecipeState = RecipeState.CREATE
)
