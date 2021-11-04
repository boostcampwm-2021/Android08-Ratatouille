package com.kdjj.domain.model

data class RecipeStep(
    val name: String,
    val type: RecipeStepType,
    val description: String,
    val imgPath: String,
    val seconds: Int
)
