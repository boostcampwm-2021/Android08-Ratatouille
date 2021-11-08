package com.kdjj.remote.entity

import com.kdjj.domain.model.RecipeStepType

data class RecipeStepEntity (
    val stepId: String = "",
    val name: String = "",
    val type: RecipeStepType = RecipeStepType.FRY,
    val description: String = "",
    val imgPath: String = "",
    val seconds: Int = 0
)