package com.kdjj.remote.dto

import com.kdjj.domain.model.RecipeType

internal data class RecipeTypeEntity(
    val id: Int = 0,
    val title: String = ""
)

internal fun RecipeTypeEntity.toDomain(): RecipeType =
    RecipeType(
        id,
        title
    )
