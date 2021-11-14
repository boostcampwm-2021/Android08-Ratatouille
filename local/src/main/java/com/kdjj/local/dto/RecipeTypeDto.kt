package com.kdjj.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kdjj.domain.model.RecipeType

@Entity(tableName = "RecipeType")
data class RecipeTypeEntity(
    @PrimaryKey
    val recipeTypeId: Long,
    val title: String
) {
    
    companion object {
        
        val defaultTypes = mutableListOf(
            RecipeTypeEntity(1, "기타"),
            RecipeTypeEntity(2, "한식"),
            RecipeTypeEntity(3, "중식"),
            RecipeTypeEntity(4, "양식"),
            RecipeTypeEntity(5, "일식"),
        )
    }
}

internal fun RecipeTypeEntity.toDomain(): RecipeType =
    RecipeType(
        recipeTypeId.toInt(),
        title
    )

internal fun RecipeType.toEntity(): RecipeTypeEntity =
    RecipeTypeEntity(
        id.toLong(),
        title
    )
