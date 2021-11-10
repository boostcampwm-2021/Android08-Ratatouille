package com.kdjj.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.kdjj.domain.model.RecipeStep
import com.kdjj.domain.model.RecipeStepType

@Entity(
    tableName = "RecipeStep",
    foreignKeys = [
        ForeignKey(
            entity = RecipeMetaEntity::class,
            parentColumns = arrayOf("recipeMetaId"),
            childColumns = arrayOf("parentRecipeId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecipeStepEntity(
    @PrimaryKey
    val stepId: String,
    val name: String,
    val order: Int,
    val type: RecipeStepType,
    val description: String,
    var imgPath: String,
    val seconds: Int,
    val parentRecipeId: String,
)

fun RecipeStepEntity.toDomain() =
    RecipeStep(
        stepId, name, type, description, imgPath, seconds
    )
