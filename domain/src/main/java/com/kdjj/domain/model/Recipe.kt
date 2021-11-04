package com.kdjj.domain.model

data class Recipe(
    val title: String,
    val type: RecipeType,
    val stuff: String,
    val imgPath: String,
    val stepList: List<RecipeStep>
)
