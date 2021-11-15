package com.kdjj.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kdjj.domain.model.RecipeType

@Entity(tableName = "RecipeType")
data class RecipeTypeDto(
    @PrimaryKey
    val recipeTypeId: Long,
    val title: String
) {
    
    companion object {
        
        val defaultTypes = mutableListOf(
            RecipeTypeDto(1, "기타"),
            RecipeTypeDto(2, "한식"),
            RecipeTypeDto(3, "중식"),
            RecipeTypeDto(4, "양식"),
            RecipeTypeDto(5, "일식"),
        )
    }
}

internal fun RecipeTypeDto.toDomain(): RecipeType =
    RecipeType(
        recipeTypeId.toInt(),
        title
    )

internal fun RecipeType.toDto(): RecipeTypeDto =
    RecipeTypeDto(
        id.toLong(),
        title
    )
