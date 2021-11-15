package com.kdjj.remote.dto

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState

internal data class RecipeDto(
    val recipeId: String = "",
    val title: String = "",
    val type: RecipeTypeDto = RecipeTypeDto(0, ""),
    val stuff: String = "",
    val imgPath: String = "",
    val stepList: List<RecipeStepDto> = listOf(),
    val authorId: String = "",
    val viewCount: Int = 0,
    val createTime: Long = 0L,
)

internal fun RecipeDto.toDomain(): Recipe =
    Recipe(
        recipeId,
        title,
        type.toDomain(),
        stuff,
        imgPath,
        stepList.map { it.toDomain() },
        authorId,
        viewCount,
        false,
        createTime,
        RecipeState.UPLOAD
    )

internal fun Recipe.toDto(): RecipeDto =
    RecipeDto(
        recipeId,
        title,
        type.toDto(),
        stuff,
        imgPath,
        stepList.map { it.toDto() },
        authorId,
        viewCount,
        createTime,
    )
