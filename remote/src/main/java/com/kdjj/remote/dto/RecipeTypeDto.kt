package com.kdjj.remote.dto

import com.kdjj.domain.model.RecipeType

internal data class RecipeTypeDto(
    val id: Int = 0,
    val title: String = ""
)

internal fun RecipeTypeDto.toDomain(): RecipeType =
    RecipeType(
        id,
        title
    )

internal fun RecipeType.toDto(): RecipeTypeDto =
    RecipeTypeDto(
        id,
        title
    )
