package com.kdjj.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.kdjj.local.model.RecipeMetaEntity

@Entity(
    tableName = "RecipeType",
    foreignKeys = [
        ForeignKey(
            entity = RecipeMetaEntity::class,
            parentColumns = arrayOf("recipeMetaId"),
            childColumns = arrayOf("parentRecipeId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecipeTypeEntity(
    @PrimaryKey
    val parentRecipeId: Long,
    val title: String
)
