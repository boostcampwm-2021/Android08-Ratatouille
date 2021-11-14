package com.kdjj.remote.dto

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState

internal data class RecipeEntity(
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

internal fun RecipeEntity.toDomain(): Recipe =
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
        state
    )
