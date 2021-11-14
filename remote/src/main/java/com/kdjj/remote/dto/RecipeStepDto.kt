package com.kdjj.remote.dto

import com.kdjj.domain.model.RecipeStep
import com.kdjj.domain.model.RecipeStepType

internal data class RecipeStepEntity(
    val stepId: String = "",
    val name: String = "",
    val type: RecipeStepType = RecipeStepType.FRY,
    val description: String = "",
    val imgPath: String = "",
    val seconds: Int = 0
)

internal fun RecipeStepEntity.toDomain(): RecipeStep =
    RecipeStep(
        stepId,
        name,
        type,
        description,
        imgPath,
        seconds
    )
